package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.InvitationValidationException;
import hng_java_boilerplate.organisation.exception.OrganisationException;
import hng_java_boilerplate.organisation.exception.response.SuccessResponse;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrganisationServiceTest {

    @InjectMocks
    private OrganisationService organisationService;
    @Mock
    private OrganisationRepository organisationRepository;
    @Mock
    private UserRepository userRepository;

    private User demoUser;
    private Organisation demoOrganisation;

    @BeforeEach
    void setUp() {
        demoUser = new User();
        demoUser.setId("558ca51d-8ecf-4766-94a4-3427c1960d8c");
        demoUser.setEmail("johnsmith@example.com");

        demoOrganisation = new Organisation();
        demoOrganisation.setId("2e94215f-4c31-429b-be66-dbacda4afe79");
        demoOrganisation.setName("John's organisation");

        List<Organisation> organisations = new ArrayList<>();
        demoUser.setOrganisations(organisations);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateAndAcceptUserToOrganisation() throws InvitationValidationException {
        String invitationLink = "http://api/hello?orgId=2e94215f-4c31-429b-be66-dbacda4afe79&expires=2025-07-23T00:00:00Z";

        Mockito.when(userRepository.findById(demoUser.getId())).thenReturn(Optional.of(demoUser));
        Mockito.when(organisationRepository.findById(demoOrganisation.getId())).thenReturn(Optional.of(demoOrganisation));
        ResponseEntity<?>responseEntity = organisationService.validateAndAcceptUserToOrganisation(invitationLink);

        Assert.assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        Assert.assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof SuccessResponse);
        SuccessResponse response = (SuccessResponse) responseEntity.getBody();
        assertEquals("Invitation accepted, you have been added to the organization", response.getMessage());
        assertTrue(demoUser.getOrganisations().contains(demoOrganisation));
        verify(userRepository, times(1)).save(demoUser);
    }

    @Test
    void shouldThrowInvitationValidationException() throws InvitationValidationException{
        String invitationLink = "http://api/hello?orgId=2e94215f-4c31-429b-be66-dbacda4afe79&expires=2020-07-23T00:00:00Z";
        Mockito.when(organisationRepository.findById("2e94215f-4c31-429b-be66-dbacda4afe79")).thenReturn(Optional.of(demoOrganisation));
        InvitationValidationException invitationValidationException = assertThrows(InvitationValidationException.class, () -> {
            organisationService.validateAndAcceptUserToOrganisation(invitationLink);
        });
        assertEquals("Invalid or expired invitation Link", invitationValidationException.getMessage());
    }


    @Test
    void shouldThrowInvitationValidationExceptionForInvalidLinkFormat() {
        String invalidInvitationLink = "http://example.com/invite?orgId=org123";
        InvitationValidationException exception = assertThrows(InvitationValidationException.class, () -> {
            organisationService.validateAndAcceptUserToOrganisation(invalidInvitationLink);
        });
        assertEquals("Invalid or expired invitation Link", exception.getMessage());
    }

}