package hng_java_boilerplate.organisation.controller;

import hng_java_boilerplate.organisation.dto.InvitationRequest;
import hng_java_boilerplate.organisation.exception.InvitationValidationException;
import hng_java_boilerplate.organisation.exception.response.SuccessResponse;
import hng_java_boilerplate.organisation.service.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1/org")
public class OrganisationController {
    @Autowired
    private OrganisationService organisationService;

    @PostMapping("/invite")
    public ResponseEntity<?> acceptInvitation(@RequestBody String invitationLink) throws InvitationValidationException, URISyntaxException {
        return new ResponseEntity<>(organisationService.validateAndAcceptUserToOrganisation(invitationLink).getBody(), HttpStatus.OK);
    }

}
