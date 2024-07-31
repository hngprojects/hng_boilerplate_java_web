package hng_java_boilerplate.organisation.controller;

import hng_java_boilerplate.organisation.response.ErrorResponse;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.UnauthorizedException;
import hng_java_boilerplate.organisation.service.OrganisationService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganisationController {

    private final OrganisationService organisationService;

    @Autowired
    public OrganisationController(OrganisationService organisationService) {
        this.organisationService = organisationService;
    }

    @GetMapping("/{org_id}")
    public ResponseEntity<?> getOrganisationById(@PathVariable("org_id") String orgId) {
        try {
            Organisation organisation = organisationService.getOrganisationById(orgId)
                    .orElseThrow(() -> new RuntimeException("Organisation not found"));

            return new ResponseEntity<>(organisation, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage(), 404), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage(), 400), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{org_id}")
    @Secured({"ROLE_ADMIN", "ROLE_SUPERADMIN"})
    public ResponseEntity<?> deleteOrganisation(@PathVariable("org_id") String orgId,
                                                @AuthenticationPrincipal UserServiceImpl userDetails) {
        try {
            User user = userDetails.getLoggedInUser();

            Organisation organisation = organisationService.getOrganisationById(orgId)
                    .orElseThrow(() -> new RuntimeException("Organisation not found"));

            organisationService.checkUserAuthorization(user, organisation);

            organisationService.softDeleteOrganisation(orgId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage(), 401), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage(), 400), HttpStatus.BAD_REQUEST);
        }
    }
}