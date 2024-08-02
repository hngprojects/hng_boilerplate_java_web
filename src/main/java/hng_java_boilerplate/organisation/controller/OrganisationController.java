package hng_java_boilerplate.organisation.controller;
import hng_java_boilerplate.organisation.dto.CreateOrganisationDTO;
import hng_java_boilerplate.organisation.dto.UpdateOrganisationDTO;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.ResourceNotFoundException;
import hng_java_boilerplate.organisation.exception.UnauthorizedException;
import hng_java_boilerplate.organisation.response.ResponseHandler;
import hng_java_boilerplate.organisation.service.OrganisationServices;
import hng_java_boilerplate.user.entity.User;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OrganisationController {

    private final OrganisationServices organisationServices;

    public OrganisationController(OrganisationServices organisationServices) {
        this.organisationServices = organisationServices;
    }

    @PostMapping("/organisations")
    public ResponseEntity<?> createOrganisation(@Valid @RequestBody CreateOrganisationDTO createOrganisationDTO, Authentication authentication) {
        try {
            User owner = (User) authentication.getPrincipal();
            Organisation savedOrg = organisationServices.createOrganisation(createOrganisationDTO, owner);

            return ResponseHandler.generateResponse("success", "organisation created successfully", savedOrg, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse("Bad Request", "Client error", HttpStatus.BAD_REQUEST);
        }
    }
    @PatchMapping("/organisations/{orgId}")
    public ResponseEntity<?> updateOrganisation(@PathVariable String orgId, @Valid @RequestBody UpdateOrganisationDTO updateOrganisationDTO, Authentication authentication) {
        try {
            User owner = (User) authentication.getPrincipal();
            Organisation updatedOrg = organisationServices.updateOrganisation(orgId, updateOrganisationDTO, owner);
            return ResponseHandler.generateResponse("success", "organisation updated successfully", updatedOrg, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return ResponseHandler.generateErrorResponse("Failed to update organization", e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            return ResponseHandler.generateErrorResponse("Failed to update organization", e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse("Failed to update organization", "Invalid organization ID format", HttpStatus.BAD_REQUEST);
        }
    }



    @DeleteMapping("/organisations/{orgId}")
    public ResponseEntity<?> deleteOrganisation(@PathVariable String orgId, Authentication authentication) {
        try {
            User owner = (User) authentication.getPrincipal();
            organisationServices.deleteOrganisation(orgId, owner);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseHandler.generateErrorResponse("Failed to delete organization", e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            return ResponseHandler.generateErrorResponse("Failed to delete organization", e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse("Failed to delete organization", "Invalid organization ID format", HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/organisations/{orgId}/users/{userId}")
    public ResponseEntity<?> removeUserFromOrganisation(@PathVariable String orgId, @PathVariable String userId, Authentication authentication) {
        try {
            User owner = (User) authentication.getPrincipal();
            organisationServices.removeUserFromOrganisation(orgId, userId, owner);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseHandler.generateErrorResponse("Failed to remove user from organization", e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            return ResponseHandler.generateErrorResponse("Failed to remove user from organization", e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse("Failed to remove user from organization", "Invalid organization or user ID format", HttpStatus.BAD_REQUEST);
        }
    }
   //Should conflict
    @GetMapping("/organisations/{orgId}")
    public ResponseEntity<?> getOrganisationById(@PathVariable String orgId) {
        try {
            Organisation organisation = organisationServices.getOrganisationById(orgId);
            return ResponseEntity.ok(organisation);
        } catch (ResourceNotFoundException e) {
            return ResponseHandler.generateErrorResponse("Failed to fetch organization details", e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse("Failed to fetch organization details", "Invalid organization ID format", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/organisations/{orgId}/users")
    public ResponseEntity<?> getUsersInOrganisation(@PathVariable String orgId, Authentication authentication) {
        try {
            User requester = (User) authentication.getPrincipal();
            List<User> users = organisationServices.getUsersInOrganisation(orgId, requester);
            return ResponseEntity.ok(users);
        } catch (ResourceNotFoundException e) {
            return ResponseHandler.generateErrorResponse("Failed to fetch users", e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            return ResponseHandler.generateErrorResponse("Unauthorized access", e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse("Failed to fetch users", "Invalid organization ID format", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/organisations/accept-invite")
    public ResponseEntity<?> acceptInvitation(@RequestParam String token, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Organisation organisation = organisationServices.acceptInvitation(token, user);

            return ResponseHandler.generateResponse("success", "Invitation accepted successfully", organisation, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return ResponseHandler.generateErrorResponse("Failed to accept invitation", e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            return ResponseHandler.generateErrorResponse("Failed to accept invitation", e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return ResponseHandler.generateErrorResponse("Failed to accept invitation", "Invalid invitation token", HttpStatus.BAD_REQUEST);
        }
    }
}



