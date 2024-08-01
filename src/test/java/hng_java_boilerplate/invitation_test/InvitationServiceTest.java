package hng_java_boilerplate.invitation_test;

import hng_java_boilerplate.organisation.dto.InvitationLink;
import hng_java_boilerplate.organisation.dto.ValidLinkResponse;
import hng_java_boilerplate.organisation.entity.Invitation;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.entity.Status;
import hng_java_boilerplate.organisation.exception.InvitationValidationException;
import hng_java_boilerplate.organisation.repository.InvitationRepository;
import hng_java_boilerplate.organisation.service.InvitationService;
import hng_java_boilerplate.organisation.service.OrganisationService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.exception.InvalidRequestException;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
public class InvitationServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private OrganisationService organisationService;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private InvitationService invitationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testAcceptUserIntoOrganization_ValidInvitation() {
        User loggedInUser = new User();
        loggedInUser.setId("user123");
        loggedInUser.setOrganisations(new ArrayList<>());

        Organisation organisation = new Organisation();
        organisation.setId("org123");

        Invitation invitation = new Invitation();
        invitation.setId(UUID.randomUUID().toString());
        invitation.setToken("valid-token");
        invitation.setUserEmail("user@example.com");
        invitation.setStatus(Status.PENDING);
        invitation.setOrganisation(organisation);
        invitation.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));

        when(userService.getLoggedInUser()).thenReturn(loggedInUser);
        when(invitationRepository.findByToken("valid-token")).thenReturn(Optional.of(invitation));
        when(organisationService.getOrganisationDetails("org123")).thenReturn(organisation);
        when(invitationRepository.save(any(Invitation.class))).thenReturn(invitation);
        when(userRepository.save(any(User.class))).thenReturn(loggedInUser);

        InvitationLink invitationLink = new InvitationLink();
        invitationLink.setInvitationLink("http://api/hello?token=valid-token");

        ResponseEntity<?> response = invitationService.acceptUserIntoOrganization(invitationLink);

        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        ValidLinkResponse validLinkResponse = (ValidLinkResponse) response.getBody();
        assertNotNull(validLinkResponse);
        assertEquals("Invitation accepted, you have been added to the organization", validLinkResponse.getMessage());
    }

    @Test
    public void testAcceptUserIntoOrganization_ExpiredToken() {
        User loggedInUser = new User();
        loggedInUser.setId("user123");
        loggedInUser.setOrganisations(new ArrayList<>());

        Organisation organisation = new Organisation();
        organisation.setId("org123");

        Invitation invitation = new Invitation();
        invitation.setId(UUID.randomUUID().toString());
        invitation.setToken("expired-token");
        invitation.setUserEmail("user@example.com");
        invitation.setStatus(Status.PENDING);
        invitation.setOrganisation(organisation);
        invitation.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));

        when(userService.getLoggedInUser()).thenReturn(loggedInUser);
        when(invitationRepository.findByToken("expired-token")).thenReturn(Optional.of(invitation));

        InvitationLink invitationLink = new InvitationLink();
        invitationLink.setInvitationLink("http://api/hello?token=expired-token");

        Exception exception = assertThrows(InvitationValidationException.class, () -> {
            invitationService.acceptUserIntoOrganization(invitationLink);
        });

        assertEquals("Invalid or expired invitation link", exception.getMessage());
    }

    @Test
    public void testAcceptUserIntoOrganization_UserAlreadyBelongsToOrg() {
        User loggedInUser = new User();
        loggedInUser.setId("user123");

        Organisation organisation = new Organisation();
        organisation.setId("org123");

        loggedInUser.setOrganisations(List.of(organisation));

        Invitation invitation = new Invitation();
        invitation.setId(UUID.randomUUID().toString());
        invitation.setToken("valid-token");
        invitation.setUserEmail("user@example.com");
        invitation.setStatus(Status.PENDING);
        invitation.setOrganisation(organisation);
        invitation.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));

        when(userService.getLoggedInUser()).thenReturn(loggedInUser);
        when(invitationRepository.findByToken("valid-token")).thenReturn(Optional.of(invitation));

        InvitationLink invitationLink = new InvitationLink();
        invitationLink.setInvitationLink("http://api/hello?token=valid-token");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            invitationService.acceptUserIntoOrganization(invitationLink);
        });

        assertEquals("User Already belong to org", exception.getMessage());
    }
}
