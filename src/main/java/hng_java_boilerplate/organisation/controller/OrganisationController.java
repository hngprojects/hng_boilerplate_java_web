package hng_java_boilerplate.organisation.controller;

import hng_java_boilerplate.organisation.dto.CreateInvitationRequestDto;
import hng_java_boilerplate.organisation.dto.CreateOrganisationRequestDto;
import hng_java_boilerplate.organisation.dto.CreateOrganisationResponseDto;
import hng_java_boilerplate.organisation.dto.InvitationLink;
import hng_java_boilerplate.organisation.dto.requestDto.SingleInviteDto;
import hng_java_boilerplate.organisation.service.InvitationService;
import hng_java_boilerplate.organisation.service.OrganisationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organisations")
@Tag(name="Organisation")
public class OrganisationController {
    private final OrganisationService organisationService;
    private final InvitationService invitationService;

    @PostMapping
    public ResponseEntity<CreateOrganisationResponseDto> createOrganisation(
            @RequestBody @Valid CreateOrganisationRequestDto orgRequest,
            Authentication activeUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                organisationService.create(orgRequest, activeUser)
        );
    }

    @PostMapping("/send-invite")
    public ResponseEntity<?> createInvitationLink(@Valid @RequestBody CreateInvitationRequestDto request){
        if (request == null){
            throw new RuntimeException("Request Body Cannot be empty");
        }
        return new ResponseEntity<>(invitationService.createInvitationLink(request).getBody(), HttpStatus.OK);
    }

    @PostMapping ("/create")
    public ResponseEntity<?> createSingleInvite(@Valid @RequestBody SingleInviteDto singleInviteDto){
        if (singleInviteDto == null){
            throw new RuntimeException("Request Body cannot be empty");
        }
        return new ResponseEntity<>(invitationService.createSingleInvite(singleInviteDto).getBody(), HttpStatus.OK);
    }

    @PostMapping("/accept")
    public ResponseEntity<?> acceptToOrganisation(@RequestBody InvitationLink invitationLink){
        if (invitationLink == null || invitationLink.getInvitationLink() == " "){
            throw new RuntimeException("Request Body cannot be empty");
        }
        return new ResponseEntity<>(invitationService.acceptUserIntoOrganization(invitationLink).getBody(),HttpStatus.OK);
    }
}
