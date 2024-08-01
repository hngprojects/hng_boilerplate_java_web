package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.email.EmailServices.EmailConsumerService;
import hng_java_boilerplate.email.EmailServices.EmailProducerService;
import hng_java_boilerplate.email.entity.EmailMessage;
import hng_java_boilerplate.organisation.dto.CreateInvitationRequestDto;
import hng_java_boilerplate.organisation.dto.MembershipInviteDto;
import hng_java_boilerplate.organisation.dto.SendInviteResponseDto;
import hng_java_boilerplate.organisation.entity.Invitation;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.entity.Status;
import hng_java_boilerplate.organisation.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final OrganisationService organisationService;
    private final InvitationRepository invitationRepository;
    private final EmailConsumerService emailConsumerService;

    @Transactional
    public ResponseEntity<?> createInvitationLink(CreateInvitationRequestDto createInvitationRequest){
//        ADMIN CONSTRAINTS, LOGGED IN USER PERMISSION
        String organisation_id = createInvitationRequest.getOrganisation_id();
        Organisation organisationDetails = organisationService.getOrganisationDetails(organisation_id);
        List<Invitation> invitationList  = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        SendInviteResponseDto sendInviteResponseDto = new SendInviteResponseDto();
        MembershipInviteDto inviteDto = new MembershipInviteDto();
        List<MembershipInviteDto> invitations = sendInviteResponseDto.getInvitations();
        List<String> emails = createInvitationRequest.getEmail();
        String invitationLink = null;
        for (String email: emails) {
            Invitation invitationTable = new Invitation();
            EmailMessage emailMessage = new EmailMessage();
            invitationTable.setId(UUID.randomUUID().toString());
            invitationTable.setToken(UUID.randomUUID().toString());
            invitationTable.setUserEmail(email);
            invitationTable.setStatus(Status.PENDING);
            invitationTable.setOrganisation(organisationDetails);
            invitationTable.setExpiresAt(Timestamp.valueOf(
                    LocalDateTime.of(
                            2024,8,3,23,30)));
            invitationTable.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            invitationTable.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            invitationLink = "http://api/hello?token=" +invitationTable.getToken() ;

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

}
