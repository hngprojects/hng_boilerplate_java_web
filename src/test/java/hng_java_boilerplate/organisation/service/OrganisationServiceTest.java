package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.organisation.dto.*;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.*;
import hng_java_boilerplate.organisation.mapper.OrganisationMapper;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.exception.UserNotFoundException;
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
    private OrganisationMapper organisationMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication activeUser;

    @InjectMocks
    private OrganisationService organisationService;

    private CreateOrganisationRequestDto orgRequest;
    private User user;
    private Organisation organisation;

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
        user.setEmail("user@example.com");

        organisation = new Organisation();
        organisation.setId("org-id");
        organisation.setName("Test Organisation");
        organisation.setOwner(user);
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

        DataDto orgDataDto = new DataDto(
                organisation.getId(),
                orgRequest.name(),
                orgRequest.description(),
                user.getId(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        when(organisationMapper.toOrgDataDto(any(Organisation.class), any(User.class))).thenReturn(orgDataDto);

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

    @Test
    void updateOrganisation_throwsExceptionIfOrganisationNotFound() {
        UpdateOrganisationRequestDto requestDto = new UpdateOrganisationRequestDto("Updated Name", "Updated Description");

        when(organisationRepository.findById("invalid-org-id")).thenReturn(Optional.empty());

        assertThrows(OrganisationNotFoundException.class, () ->
                organisationService.update(requestDto, "invalid-org-id", activeUser)
        );
    }

    @Test
    void updateOrganisation_updatesOrganisationAndReturnsResponse() {
        UpdateOrganisationRequestDto requestDto = new UpdateOrganisationRequestDto("Updated Name", "Updated Description");

        when(organisationRepository.findById("org-id")).thenReturn(Optional.of(organisation));
        when(activeUser.getPrincipal()).thenReturn(user);

        UpdateOrganisationResponseDto responseDto = organisationService.update(requestDto, "org-id", activeUser);

        assertNotNull(responseDto);
        assertEquals("Organisation updated successfully", responseDto.message());
        assertEquals("Updated Name", responseDto.org().name());
    }

    @Test
    void userOrganisations_throwsExceptionIfUserNotFound() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        when(activeUser.getName()).thenReturn("user@example.com");

        assertThrows(UserNotFoundException.class, () ->
                organisationService.userOrganisations(activeUser)
        );
    }

    @Test
    void userOrganisations_returnsOrganisationsForUser() {
        user.setOrganisations(List.of(organisation));

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(activeUser.getName()).thenReturn("user@example.com");

        OrganisationsListDto orgListDto = new OrganisationsListDto(organisation.getId(), organisation.getName(), organisation.getDescription());
        when(organisationMapper.mapToOranisationListDto(organisation)).thenReturn(orgListDto);

        UserOrganisationsResponseDto responseDto = organisationService.userOrganisations(activeUser);

        assertNotNull(responseDto);
        assertEquals("success", responseDto.status());
        assertEquals("Organisations retrieved successfully", responseDto.message());
        assertNotNull(responseDto.data());
        assertFalse(responseDto.data().organisations().isEmpty());
        assertEquals(1, responseDto.data().organisations().size());
        assertEquals("Test Organisation", responseDto.data().organisations().get(0).name());
    }
}