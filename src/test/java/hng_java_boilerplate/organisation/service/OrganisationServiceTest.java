package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.organisation.dto.CreateOrganisationRequestDto;
import hng_java_boilerplate.organisation.dto.CreateOrganisationResponseDto;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.ExceptionResponse;
import hng_java_boilerplate.organisation.exception.OrgGlobalExceptionHandler;
import hng_java_boilerplate.organisation.exception.OrganisationNameAlreadyExistsException;
import hng_java_boilerplate.organisation.exception.ValidationError;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganisationServiceTest {

    @Mock
    private OrganisationRepository organisationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication activeUser;

    @InjectMocks
    private OrganisationService organisationService;

    private CreateOrganisationRequestDto orgRequest;
    private User user;

    @BeforeEach
    void setUp() {
        orgRequest = new CreateOrganisationRequestDto(
                "vpay",
                "A payment platform",
                "info@vpay.com",
                "Finance",
                "Private",
                "USA",
                "123 Payment St.",
                "CA"
        );
        user = new User();
        user.setId("user-123");
//        when(activeUser.getPrincipal()).thenReturn(user);
    }

    @Test
    void create_shouldThrowOrganisationNameAlreadyExistsException_whenOrganisationWithNameAlreadyExists() {
        when(organisationRepository.findByName(orgRequest.name())).thenReturn(Optional.of(new Organisation()));

        OrganisationNameAlreadyExistsException exception = assertThrows(
                OrganisationNameAlreadyExistsException.class,
                () -> organisationService.create(orgRequest, activeUser)
        );

        assertEquals("Sorry, an Organisation with NAME::vpay already exists", exception.getMessage());
        verify(organisationRepository, times(1)).findByName(orgRequest.name());
        verifyNoMoreInteractions(organisationRepository);
        verifyNoInteractions(userRepository);
    }

    @Test
    void create_shouldCreateOrganisationSuccessfully_whenNameIsUnique() {
        when(organisationRepository.findByName(orgRequest.name())).thenReturn(Optional.empty());
        when(activeUser.getPrincipal()).thenReturn(user);

        CreateOrganisationResponseDto response = organisationService.create(orgRequest, activeUser);

        assertNotNull(response);
        assertEquals("success", response.status());
        assertEquals("Organisation created successfully", response.message());
        assertEquals(201, response.status_code());
        assertNotNull(response.data());
        assertEquals(orgRequest.name(), response.data().name());
        verify(organisationRepository, times(1)).save(any(Organisation.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void create_shouldThrowValidationException_whenRequestIsInvalid() {
        CreateOrganisationRequestDto invalidRequest = new CreateOrganisationRequestDto(
                "",  // Invalid name
                "A payment platform",
                "info@vpay.com",
                "Finance",
                "Private",
                "USA",
                "123 Payment St.",
                "CA"
        );

        BindingResult bindingResult = new BeanPropertyBindingResult(invalidRequest, "createOrganisationRequestDto");
        bindingResult.rejectValue("name", "NotEmpty", "Name must not be empty");

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        OrgGlobalExceptionHandler handler = new OrgGlobalExceptionHandler();
        ResponseEntity<ExceptionResponse> responseEntity = handler.handleValidationExceptions(ex);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        List<ValidationError> errors = responseEntity.getBody().errors();
        assertEquals(1, errors.size());
        assertEquals("name", errors.get(0).field());
        assertEquals("Name must not be empty", errors.get(0).message());
    }

}