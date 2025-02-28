package hng_java_boilerplate.organisation.controller;

import hng_java_boilerplate.organisation.dto.*;
import hng_java_boilerplate.organisation.interfaces.AddUserResponse;
import hng_java_boilerplate.organisation.service.AddUsersToOrganisationService;
import hng_java_boilerplate.organisation.service.OrganisationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organisations")
@Tag(name="Organisation")
public class OrganisationController {
    private final OrganisationService organisationService;
    private final AddUsersToOrganisationService addUsersToOrganisationService;

    @PostMapping
    public ResponseEntity<CreateOrganisationResponseDto> createOrganisation(
            @RequestBody @Valid CreateOrganisationRequestDto orgRequest,
            Authentication activeUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                organisationService.create(orgRequest, activeUser)
        );
    }

    @PostMapping("/{organisationId}/users")
    public ResponseEntity<?> addUserToOrganisation(
            @PathVariable("organisationId") String organisationId,
            @RequestBody @Valid AddUserRequestDTO orgRequest,
            Authentication authenticatedUser
    ) {
        AddUserResponse response = addUsersToOrganisationService.addUserToOrganisation(organisationId, orgRequest,
                authenticatedUser);

        if (response instanceof AddUserExceptionDto) {
            return ResponseEntity.status(((AddUserExceptionDto) response).status_code()).body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
