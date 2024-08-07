package hng_java_boilerplate.invitation_test;

import hng_java_boilerplate.email.EmailServices.EmailConsumerService;
import hng_java_boilerplate.email.entity.EmailMessage;
import hng_java_boilerplate.helpCenter.topic.exceptions.ResourceNotFoundException;
import hng_java_boilerplate.organisation.dto.CreateInvitationRequestDto;
import hng_java_boilerplate.organisation.dto.InvitationLink;
import hng_java_boilerplate.organisation.dto.ValidLinkResponse;
import hng_java_boilerplate.organisation.dto.requestDto.SendInviteResponseDto;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class InvitationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrganisationService organisationService;

    @Mock
    private EmailConsumerService emailConsumerService;

    @InjectMocks
    private InvitationService invitationService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAcceptUserIntoOrganization_Success() throws Exception {
        String token = "valid-token";
        User user = new User();
        List<Organisation> organisations = new ArrayList<>();
        user.setOrganisations(organisations);

        Organisation organisation = new Organisation();
        Invitation invitation = new Invitation();
        invitation.setToken(token);
        invitation.setStatus(Status.PENDING);
        invitation.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
        invitation.setOrganisation(organisation);

        when(userService.getLoggedInUser()).thenReturn(user);
        when(invitationRepository.findByToken(token)).thenReturn(Optional.of(invitation));
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = invitationService.acceptUserIntoOrganization(token);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        ValidLinkResponse validLinkResponse = (ValidLinkResponse) response.getBody();
        assertEquals("Invitation accepted, you have been added to the organization", validLinkResponse.getMessage());
    }

    @Test
    void testAcceptUserIntoOrganization_InvalidToken() {
        String token = "invalid-token";

        when(invitationRepository.findByToken(token)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            invitationService.acceptUserIntoOrganization(token);
        });
    }

    @Test
    void testAcceptUserIntoOrganization_ExpiredToken() {
        String token = "valid-token";
        User user = new User();
        Organisation organisation = new Organisation();
        Invitation invitation = new Invitation();
        invitation.setToken(token);
        invitation.setStatus(Status.PENDING);
        invitation.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        invitation.setOrganisation(organisation);

        when(userService.getLoggedInUser()).thenReturn(user);
        when(invitationRepository.findByToken(token)).thenReturn(Optional.of(invitation));

        assertThrows(InvitationValidationException.class, () -> {
            invitationService.acceptUserIntoOrganization(token);
        });
    }

    @Test
    void testAcceptUserIntoOrganization_UserAlreadyInOrganization() {
        String token = "valid-token";
        User user = new User();
        Organisation organisation = new Organisation();
        List<Organisation> organisations = new ArrayList<>();
        organisations.add(organisation);
        user.setOrganisations(organisations);

        Invitation invitation = new Invitation();
        invitation.setToken(token);
        invitation.setStatus(Status.PENDING);
        invitation.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
        invitation.setOrganisation(organisation);

        when(userService.getLoggedInUser()).thenReturn(user);
        when(invitationRepository.findByToken(token)).thenReturn(Optional.of(invitation));

        assertThrows(Exception.class, () -> {
            invitationService.acceptUserIntoOrganization(token);
        });
    }

    @Test
    void testCreateInvitationLink() {
        CreateInvitationRequestDto requestDto = new CreateInvitationRequestDto();
        requestDto.setEmail(List.of("test1@example.com", "test2@example.com"));
        requestDto.setOrganisation_id("org-id");

        Organisation organisation = new Organisation();
        organisation.setId("org-id");
        organisation.setName("Test Organisation");

        when(organisationService.getOrganisationDetails(anyString())).thenReturn(organisation);

        ResponseEntity<?> response = invitationService.createInvitationLink(requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        SendInviteResponseDto responseBody = (SendInviteResponseDto) response.getBody();
        assertEquals("Invitation(s) sent Successfully", responseBody.getMessages());
        assertEquals(2, responseBody.getInvitations().size());

        verify(invitationRepository, times(1)).saveAll(anyList());
        verify(emailConsumerService, times(2)).receiveMessage(any(EmailMessage.class));
    }
}
