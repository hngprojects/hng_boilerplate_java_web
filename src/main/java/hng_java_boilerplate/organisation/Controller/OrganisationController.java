package hng_java_boilerplate.organisation.controller;

import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.service.OrganisationService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganisationController {

    private final OrganisationService organisationService;
    private final UserService userService;

    @Autowired
    public OrganisationController(OrganisationService organisationService, UserService userService) {
        this.organisationService = organisationService;
        this.userService = userService;
    }

    @DeleteMapping("/{org_id}")
    public ResponseEntity<?> deleteOrganisation(@PathVariable("org_id") String orgId,
                                                @AuthenticationPrincipal UserServiceImpl userDetails) {
        try {
            User user = userDetails.getLoggedInUser();

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
       gi private int statusCode;

        public ErrorResponse(String message, int statusCode) {
            this.message = message;
            this.statusCode = statusCode;
        }


    }
}