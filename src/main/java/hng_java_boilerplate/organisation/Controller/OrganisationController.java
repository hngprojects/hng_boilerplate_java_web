package hng_java_boilerplate.organisation.Controller;

import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.UnauthorizedException;
import hng_java_boilerplate.organisation.service.OrganisationService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/organizations")
public class OrganisationController {

    private final OrganisationService organisationService;

    private final UserRepository userRepository;

    public OrganisationController(OrganisationService organisationService, UserRepository userRepository) {
        this.organisationService = organisationService;
        this.userRepository = userRepository;
    }

    @DeleteMapping("/{org_id}")
    public ResponseEntity<?> deleteOrganisation(@PathVariable("org_id") String orgId,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByName(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Organisation organisation = organisationService.findOrganisationById(orgId)
                    .orElseThrow(() -> new RuntimeException("Organisation not found"));

            if (!organisation.getUsers().contains(user)) {
                return new ResponseEntity<>(new ErrorResponse("User not authorized to delete this organization", 401), HttpStatus.UNAUTHORIZED);
            }

            organisationService.softDeleteOrganisation(orgId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage(), 400), HttpStatus.BAD_REQUEST);
        }
    }

    private static class ErrorResponse {
        private String message;
        private int statusCode;

        public ErrorResponse(String message, int statusCode) {
            this.message = message;
            this.statusCode = statusCode;
        }


    }
}