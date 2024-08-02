package hng_java_boilerplate.organisation.serviceImpl;


import hng_java_boilerplate.email.EmailServices.EmailConsumerService;
import hng_java_boilerplate.organisation.dto.CreateOrganisationDTO;
import hng_java_boilerplate.organisation.dto.UpdateOrganisationDTO;
import hng_java_boilerplate.organisation.entity.Invitation;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.ResourceNotFoundException;
import hng_java_boilerplate.organisation.exception.UnauthorizedException;
import hng_java_boilerplate.organisation.repository.InvitationRepository;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.organisation.service.OrganisationServices;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.util.ValidPassword;
import hng_java_boilerplate.waitlist.service.EmailService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrganisationServiceImpl implements OrganisationServices {

    private final OrganisationRepository organisationRepository;
    private final UserRepository userRepository;

     private final EmailService emailService;
     private final InvitationRepository invitationRepository;

    @Value("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY")
    private String secretKey;

    public OrganisationServiceImpl(OrganisationRepository organisationRepository, UserRepository userRepository,EmailService emailService,InvitationRepository invitationRepository) {
        this.organisationRepository = organisationRepository;
        this.userRepository= userRepository;
        this.emailService=emailService;
        this.invitationRepository= invitationRepository;
    }

    @Override
    public Organisation createOrganisation(CreateOrganisationDTO dto, User owner) {
        Organisation organisation = new Organisation();
        organisation.setName(dto.getName());
        organisation.setDescription(dto.getDescription());
        organisation.setEmail(dto.getEmail());
        organisation.setIndustry(dto.getIndustry());
        organisation.setType(dto.getType());
        organisation.setCountry(dto.getCountry());
        organisation.setAddress(dto.getAddress());
        organisation.setState(dto.getState());
        organisation.setOwner(owner);
        organisation.setCreatedAt(LocalDateTime.now());
        organisation.setUpdatedAt(LocalDateTime.now());

        return organisationRepository.save(organisation);
    }


    @Override
    public boolean deleteOrganisation(String orgId, User owner) {
        Optional<Organisation> organisationOpt = organisationRepository.findById(orgId);
        if (organisationOpt.isPresent()) {
            Organisation organisation = organisationOpt.get();
            if (organisation.getOwner().getId().equals(owner.getId())) {
                organisation.setDeleted(true);
                organisationRepository.save(organisation);
                return true;
            } else {
                throw new UnauthorizedException("User not authorized to delete this organization");
            }
        } else {
            throw new ResourceNotFoundException("Invalid organization ID");
        }
    }
    @Override
    public boolean removeUserFromOrganisation(String orgId, String userId, User owner) {
        Optional<Organisation> organisationOpt = organisationRepository.findById(orgId);
        if (organisationOpt.isPresent()) {
            Organisation organisation = organisationOpt.get();
            if (organisation.getOwner().getId().equals(owner.getId())) {
                Optional<User> userOpt = userRepository.findById(userId);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    if (organisation.getUsers().contains(user)) {
                        organisation.getUsers().remove(user);
                        organisationRepository.save(organisation);
                        return true;
                    } else {
                        throw new ResourceNotFoundException("User not found in the organization");
                    }
                } else {
                    throw new ResourceNotFoundException("Invalid user ID");
                }
            } else {
                throw new UnauthorizedException("User not authorized to remove users from this organization");
            }
        } else {
            throw new ResourceNotFoundException("Invalid organization ID");
        }
    }
    @Override
    public Organisation updateOrganisation(String orgId, UpdateOrganisationDTO dto, User owner) {
        Optional<Organisation> organisationOpt = organisationRepository.findById(orgId);
        if (organisationOpt.isPresent()) {
            Organisation organisation = organisationOpt.get();
            if (organisation.getOwner().getId().equals(owner.getId())) {
                organisation.setName(dto.getName());
                organisation.setDescription(dto.getDescription());
                organisation.setEmail(dto.getEmail());
                organisation.setIndustry(dto.getIndustry());
                organisation.setType(dto.getType());
                organisation.setCountry(dto.getCountry());
                organisation.setAddress(dto.getAddress());
                organisation.setState(dto.getState());
                organisation.setUpdatedAt(LocalDateTime.now()); // Update the updatedAt field

                return organisationRepository.save(organisation);
            } else {
                throw new UnauthorizedException("User not authorized to update this organization");
            }
        } else {
            throw new ResourceNotFoundException("Invalid organization ID");
        }
    }

    @Override
    public Organisation getOrganisationById(String orgId) {
        return organisationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with ID: " + orgId));
    }


    @Override
    public List<User> getUsersInOrganisation(String orgId, User requester) {
        Optional<Organisation> organisationOpt = organisationRepository.findById(orgId);
        if (organisationOpt.isPresent()) {
            Organisation organisation = organisationOpt.get();
            if (organisation.getOwner().equals(requester) || organisation.getUsers().contains(requester)) {
                return organisation.getUsers();
            } else {
                throw new UnauthorizedException("User not authorized to view users in this organization");
            }
        } else {
            throw new ResourceNotFoundException("Organization not found with ID: " + orgId);
        }
    }

    @Override
    public boolean sendInviteToUser(String orgId, String email, User owner) {
        Optional<Organisation> organisationOpt = organisationRepository.findById(orgId);
        if (organisationOpt.isPresent()) {
            Organisation organisation = organisationOpt.get();
            if (organisation.getOwner().getId().equals(owner.getId())) {
                String inviteLink = generateInviteLink(orgId, email);
                emailService.sendConfirmationEmail(email, "Invitation to Join Organization", "Please click the following link to join: " + inviteLink);
                return true;
            } else {
                throw new UnauthorizedException("User not authorized to send invites for this organization");
            }
        } else {
            throw new ResourceNotFoundException("Invalid organization ID");
        }
    }
    private String generateInviteLink(String orgId, String email) {
        long timestamp = new Date().getTime();
        String data = orgId + email + timestamp + secretKey;
        String token = generateSHA256Hash(data);
        String encodedEmail = Base64.getUrlEncoder().encodeToString(email.getBytes(StandardCharsets.UTF_8));
        return String.format("http://org.com/invite?orgId=%s&email=%s&token=%s&timestamp=%d", orgId, encodedEmail, token, timestamp);
    }

    private String generateSHA256Hash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
    @Override
    public Organisation acceptInvitation(String token, User user) {
        Optional<Invitation> invitationOpt = invitationRepository.findByToken(token);
        if (invitationOpt.isPresent()) {
            Invitation invitation = invitationOpt.get();
            Organisation organisation = invitation.getOrganisation();
            if (!organisation.getUsers().contains(user)) {
                organisation.getUsers().add(user);
                organisationRepository.save(organisation);
                invitationRepository.delete(invitation);
                return organisation;
            } else {
                throw new UnauthorizedException("User already a member of the organization");
            }
        } else {
            throw new ResourceNotFoundException("Invalid invitation token");
        }
    }
}





