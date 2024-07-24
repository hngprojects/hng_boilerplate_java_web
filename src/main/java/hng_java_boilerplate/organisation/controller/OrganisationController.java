package hng_java_boilerplate.organisation.controller;

import hng_java_boilerplate.organisation.dto.request.AddUserRequest;
import hng_java_boilerplate.organisation.dto.response.AddUserResponse;
import hng_java_boilerplate.organisation.service.OrganisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organisations")
public class OrganisationController {
    private final OrganisationService organisationService;

    @Secured("ROLE_ADMIN")
    @PostMapping("/{orgId}/users")
    public ResponseEntity<AddUserResponse> addUserToOrg(@PathVariable String orgId, @RequestBody AddUserRequest request) {
        return ResponseEntity.ok(organisationService.addUserToOrganisation(request, orgId));
    }
}
