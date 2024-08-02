package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.email.EmailServices.EmailConsumerService;
import hng_java_boilerplate.email.entity.EmailMessage;
import hng_java_boilerplate.helpCenter.topic.exceptions.ResourceNotFoundException;
import hng_java_boilerplate.organisation.dto.*;
import hng_java_boilerplate.organisation.dto.requestDto.MembershipInviteDto;
import hng_java_boilerplate.organisation.dto.requestDto.SendInviteResponseDto;
import hng_java_boilerplate.organisation.dto.requestDto.SingleInviteDto;
import hng_java_boilerplate.organisation.entity.Invitation;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.entity.Status;
import hng_java_boilerplate.organisation.exception.InvitationValidationException;
import hng_java_boilerplate.organisation.exception.InviteErrorResponse;
import hng_java_boilerplate.organisation.repository.InvitationRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.exception.InvalidRequestException;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final OrganisationService organisationService;
    private final InvitationRepository invitationRepository;
    private final EmailConsumerService emailConsumerService;

    private final UserRepository userRepository;

    private final UserService userService;


    @Transactional
    public ResponseEntity<?>createSingleInvite(SingleInviteDto singleInviteDto){
        User loggedInUser = userService.getLoggedInUser();
        loggedInUser.getUserRole();
        Organisation organisationDetails = organisationService.getOrganisationDetails(
                singleInviteDto.getOrganisation_id());

        Invitation invitationTable = new Invitation();
        invitationTable.setId(UUID.randomUUID().toString());
        invitationTable.setToken(UUID.randomUUID().toString());
        invitationTable.setUserEmail(singleInviteDto.getEmail());
        invitationTable.setStatus(Status.PENDING);
        invitationTable.setOrganisation(organisationDetails);
        invitationTable.setExpiresAt(Timestamp.valueOf(
                LocalDateTime.now().plusDays(2)));
        invitationTable.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        invitationTable.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        InvitationLink invitationLink = new InvitationLink();
        invitationLink.setInvitationLink("http://invite/accept?token=" + invitationTable.getToken());
        List<InvitationLink>iv = new ArrayList<>();
        iv.add(invitationLink);
        SingleResponseDto singleResponseDto = new SingleResponseDto();
        singleResponseDto.setMessage("Invitation link created successfully");
        singleResponseDto.setData(iv);
        singleResponseDto.setStatus(HttpStatus.CREATED.value());
        invitationRepository.save(invitationTable);
        return new ResponseEntity<>(singleResponseDto, HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<?> createInvitationLink(CreateInvitationRequestDto createInvitationRequest){
//        ADMIN CONSTRAINTS, LOGGED IN USER PERMISSION
        List<Invitation> invitationList  = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        MembershipInviteDto inviteDto = new MembershipInviteDto();
        SendInviteResponseDto sendInviteResponseDto = new SendInviteResponseDto();
        String invitationLink = null;

        List<MembershipInviteDto> invitations = sendInviteResponseDto.getInvitations();
        List<String> emails = createInvitationRequest.getEmail();
        String organisation_id = createInvitationRequest.getOrganisation_id();
        Organisation organisationDetails = organisationService.getOrganisationDetails(organisation_id);

        for (String email: emails) {
            Invitation invitationTable = new Invitation();
            EmailMessage emailMessage = new EmailMessage();
            invitationTable.setId(UUID.randomUUID().toString());
            invitationTable.setToken(UUID.randomUUID().toString());
            invitationTable.setUserEmail(email);
            invitationTable.setStatus(Status.PENDING);
            invitationTable.setOrganisation(organisationDetails);
            invitationTable.setExpiresAt(Timestamp.valueOf(
                    LocalDateTime.now().plusDays(2)));
            invitationTable.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            invitationTable.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            invitationLink = "http://invite/accept?token=" +invitationTable.getToken() ;

            emailMessage.setTo(email);
            emailMessage.setSubject("Invitation Link to Organisation");
            emailMessage.setText(invitationLink);
            emailConsumerService.receiveMessage(
                  emailMessage);
            inviteDto.setOrganization(organisationDetails.getName());
            inviteDto.setEmail(email);
            inviteDto.setExpires_at(invitationTable.getExpiresAt().toString());
            invitations.add(inviteDto);
            invitationList.add(invitationTable);
            strings.add(invitationLink);
        }
        sendInviteResponseDto.setMessages("Invitation(s) sent Successfully");
        sendInviteResponseDto.setInvitations(invitations);
        invitationRepository.saveAll(invitationList);
        return new ResponseEntity<>(sendInviteResponseDto, HttpStatus.OK);
    }

    public ResponseEntity<?> acceptUserIntoOrganization(InvitationLink invitationLink) throws Exception {
        User loggedInUser = userService.getLoggedInUser();
        InviteErrorResponse inviteErrorResponse = new InviteErrorResponse();
        List<String> error = new ArrayList<>();
        String invitationToken = null;

        Pattern pattern = Pattern.compile("token=([\\w-]+)");
        Matcher matcher = pattern.matcher(invitationLink.getInvitationLink());

        if (matcher.find()){
            invitationToken = matcher.group(1);
        }else {
            error.add("Invalid link format");
            inviteErrorResponse.setMessage("Invalid or expired invitation link");
            inviteErrorResponse.setError(error);
            inviteErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            throw new InvitationValidationException(
                   inviteErrorResponse.getMessage(),
                    inviteErrorResponse.getError(),
                    inviteErrorResponse.getStatus()
            );
        }
        Invitation invitation = invitationRepository.findByToken(invitationToken)
                .orElseThrow(() -> new ResourceNotFoundException("Doesn't Exist"));
        if (invitation.getStatus() == Status.EXPIRED || invitation.getStatus() ==Status.ACCEPTED){
            throw new InvalidRequestException("Invalid Invite Link");
        }
        if (Timestamp.valueOf(LocalDateTime.now()).after(invitation.getExpiresAt())){
            error.add("Expired InvitationLink");
            inviteErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            throw new InvitationValidationException("Invalid or expired invitation link",
                    inviteErrorResponse.getError(),
                    inviteErrorResponse.getStatus());
        }
        Organisation organisation = invitation.getOrganisation();
        List<Organisation> loggedInUserOrganisations = loggedInUser.getOrganisations();

        if (loggedInUserOrganisations.contains(organisation)){
            throw new Exception("User Already belong to org");
        }
       loggedInUserOrganisations.add(organisation);
        loggedInUser.setOrganisations(loggedInUserOrganisations);
        invitation.setStatus(Status.ACCEPTED);
        invitationRepository.save(invitation);
        userRepository.save(loggedInUser);
        ValidLinkResponse validLinkResponse = new ValidLinkResponse("Invitation accepted, you have been added to the organization",
                HttpStatus.OK.value());
        return new ResponseEntity<>(validLinkResponse, HttpStatus.ACCEPTED);
    }

}
