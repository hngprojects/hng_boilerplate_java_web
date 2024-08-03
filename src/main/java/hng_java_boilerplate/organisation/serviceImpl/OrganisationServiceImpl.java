package hng_java_boilerplate.organisation.serviceImpl;

import hng_java_boilerplate.organisation.dto.CreateOrganisationDTO;
import hng_java_boilerplate.organisation.dto.CreateRoleDTO;
import hng_java_boilerplate.organisation.dto.UpdateOrganisationDTO;
import hng_java_boilerplate.organisation.entity.Invitation;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.entity.Permission;
import hng_java_boilerplate.organisation.exception.ResourceNotFoundException;
import hng_java_boilerplate.organisation.exception.UnauthorizedException;
import hng_java_boilerplate.organisation.repository.InvitationRepository;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.organisation.repository.PermissionRepository;
import hng_java_boilerplate.organisation.repository.RoleRepository;
import hng_java_boilerplate.organisation.service.OrganisationServices;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.organisation.entity.Role;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.waitlist.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrganisationServiceImpl implements OrganisationServices {

    private final OrganisationRepository organisationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final InvitationRepository invitationRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Value("wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY")
    private String secretKey;

    public OrganisationServiceImpl(OrganisationRepository organisationRepository, UserRepository userRepository,
                                   EmailService emailService, InvitationRepository invitationRepository,
                                   RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.organisationRepository = organisationRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.invitationRepository = invitationRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
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
                organisation.setUpdatedAt(LocalDateTime.now());

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
        if (requester == null) {
            throw new UnauthorizedException("Requesting user cannot be null");
        }

        Organisation organisation = organisationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with ID: " + orgId));

        if (organisation.getOwner().equals(requester) || organisation.getUsers().contains(requester)) {
            return organisation.getUsers();
        } else {
            throw new UnauthorizedException("User not authorized to view users in this organization");
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

    @Override
    public Role createRoleInOrganisation(String orgId, CreateRoleDTO dto, User owner) {
        Optional<Organisation> organisationOpt = organisationRepository.findById(orgId);
        if (organisationOpt.isPresent()) {
            Organisation organisation = organisationOpt.get();
            if (organisation.getOwner().getId().equals(owner.getId())) {
                Role role = new Role();
                role.setName(dto.getName());
                role.setDescription(dto.getDescription());
                role.setOrganisation(organisation);
                return roleRepository.save(role);
            } else {
                throw new UnauthorizedException("User not authorized to create roles in this organization");
            }
        } else {
            throw new ResourceNotFoundException("Invalid organization ID");
        }
    }

    @Override
    public List<Role> getAllRolesInOrganisation(String orgId, User requester) {
        Optional<Organisation> organisationOpt = organisationRepository.findById(orgId);
        if (organisationOpt.isPresent()) {
            Organisation organisation = organisationOpt.get();
            if (organisation.getOwner().getId().equals(requester.getId())) {
                return roleRepository.findByOrganisationId(orgId);
            } else {
                throw new UnauthorizedException("User not authorized to view roles in this organization");
            }
        } else {
            throw new ResourceNotFoundException("Invalid organization ID");
        }
    }

    @Override
    public Role getRoleDetails(String orgId, String roleId, User owner) {
        Optional<Organisation> organisationOpt = organisationRepository.findById(orgId);
        if (organisationOpt.isPresent()) {
            Organisation organisation = organisationOpt.get();
            if (organisation.getOwner().getId().equals(owner.getId())) {
                Optional<Role> roleOpt = roleRepository.findByIdAndOrganisationId(roleId, orgId);
                if (roleOpt.isPresent()) {
                    return roleOpt.get();
                } else {
                    throw new ResourceNotFoundException("Role not found in the organization");
                }
            } else {
                throw new UnauthorizedException("User not authorized to view roles in this organization");
            }
        } else {
            throw new ResourceNotFoundException("Invalid organization ID");
        }
    }

    @Override
    public Role updateRoleInOrganisation(String orgId, String roleId, Role updatedRoleData, User owner) {
        Optional<Organisation> organisationOpt = organisationRepository.findById(orgId);
        if (organisationOpt.isPresent()) {
            Organisation organisation = organisationOpt.get();
            if (organisation.getOwner().getId().equals(owner.getId())) {
                Optional<Role> roleOpt = roleRepository.findByIdAndOrganisationId(roleId, orgId);
                if (roleOpt.isPresent()) {
                    Role role = roleOpt.get();
                    role.setName(updatedRoleData.getName());
                    role.setDescription(updatedRoleData.getDescription());
                    return roleRepository.save(role);
                } else {
                    throw new ResourceNotFoundException("Role not found in the organization");
                }
            } else {
                throw new UnauthorizedException("User not authorized to update roles in this organization");
            }
        } else {
            throw new ResourceNotFoundException("Invalid organization ID");
        }
    }

    @Override
    public Role updateRolePermissions(String orgId, String roleId, Set<String> permissionIds, User owner) {
        Optional<Organisation> organisationOpt = organisationRepository.findById(orgId);
        if (!organisationOpt.isPresent()) {
            throw new ResourceNotFoundException("Invalid organization ID");
        }

        Organisation organisation = organisationOpt.get();
        if (!organisation.getOwner().getId().equals(owner.getId())) {
            throw new UnauthorizedException("User not authorized to update roles in this organization");
        }

        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new ResourceNotFoundException("Invalid role ID");
        }

        Role role = roleOpt.get();
        Set<Permission> permissions = new HashSet<>();
        for (String permissionId : permissionIds) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Permission not found: " + permissionId));
            permissions.add(permission);
        }
        role.setPermissions(List.copyOf(permissions));
        return roleRepository.save(role);
    }
}
