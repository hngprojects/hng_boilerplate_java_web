package hng_java_boilerplate.organisation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/org")
public class OrganisationController {

    @PostMapping("/invite")
    public ResponseEntity<?> acceptInvitation(@RequestBody String invitationLink){
        if (invitationLink == " " || invitationLink == null){

        }

        return null;
    }

}
