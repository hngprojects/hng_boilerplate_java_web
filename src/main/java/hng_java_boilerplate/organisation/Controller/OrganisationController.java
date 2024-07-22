package hng_java_boilerplate.organisation.Controller;

import hng_java_boilerplate.organisation.exception.UnauthorizedException;
import hng_java_boilerplate.organisation.service.OrganisationService;
import hng_java_boilerplate.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganisationController {

    @Autowired
    private OrganisationService organisationService;

    @DeleteMapping("/{org_id}")
    public ResponseEntity<?> deleteOrganisation(@PathVariable("org_id") String orgId, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            organisationService.deleteOrganisation(orgId, user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse("Failed to delete organization", "Invalid organization ID", 400), HttpStatus.BAD_REQUEST);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Failed to delete organization", "Invalid organization ID", 404), HttpStatus.NOT_FOUND);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(new ErrorResponse("Failed to delete organization", "User not authorized to delete this organization", 401), HttpStatus.UNAUTHORIZED);
        }
    }

    static class ErrorResponse {
        private String message;
        private String error;
        private int statusCode;

        public ErrorResponse(String message, String error, int statusCode) {
            this.message = message;
            this.error = error;
            this.statusCode = statusCode;
        }


    }
}