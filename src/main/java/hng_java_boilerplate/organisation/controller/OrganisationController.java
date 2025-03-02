package hng_java_boilerplate.organisation.controller;

import hng_java_boilerplate.organisation.dto.CreateOrganisationRequestDto;
import hng_java_boilerplate.organisation.dto.CreateOrganisationResponseDto;
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
@Tag(name = "Organisation")
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

    @GetMapping("/{org_id}/users")
    public ResponseEntity<?> getOrganisationUsers(@PathVariable(name = "org_id") String orgId,
                                              @RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "10") int pageSize) {
        try {
            return ResponseEntity.ok(organisationService.getOrganisationUsers(orgId, page, pageSize));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
