package hng_java_boilerplate.organisation.controller;

import hng_java_boilerplate.organisation.common.PageResponse;
import hng_java_boilerplate.organisation.dto.*;
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

    @PostMapping
    public ResponseEntity<CreateOrganisationResponseDto> createOrganisation(
            @RequestBody @Valid CreateOrganisationRequestDto orgRequest,
            Authentication activeUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                organisationService.create(orgRequest, activeUser)
        );
    }

    @PatchMapping("/{orgId}")
    public ResponseEntity<UpdateOrganisationResponseDto> updateOrganisation(
            @RequestBody @Valid UpdateOrganisationRequestDto orgRequest,
            @PathVariable(name = "orgId") String orgId,
            Authentication activeUser
    ) {
        return ResponseEntity.ok(organisationService.update(orgRequest, orgId, activeUser));
    }

    @GetMapping("/me")
    public ResponseEntity<UserOrganisationsResponseDto> userOrganisations(
            Authentication activeUser
    ) {
        return ResponseEntity.ok(organisationService.userOrganisations(activeUser));
    }

    @GetMapping("/{orgId}/users")
    public ResponseEntity<PageResponse<OrgUsersPaginatedResponseDto>> fetchAllUsers(
            @PathVariable(name = "orgId") String orgId,
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "page_size", defaultValue = "10", required = false) int page_size,
            Authentication activeUser
    ) {
        return ResponseEntity.ok(organisationService.fetchAllUsers(orgId, page, page_size, activeUser));
    }

    @GetMapping("/{orgId}")
    public ResponseEntity<CreateOrganisationResponseDto> findOrganisationById(
            @PathVariable(name = "orgId") String orgId
    ) {
        return ResponseEntity.ok(organisationService.findOrganisationById(orgId));
    }
}
