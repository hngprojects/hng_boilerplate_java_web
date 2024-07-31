package hng_java_boilerplate.organisation.controller;

import hng_java_boilerplate.organisation.dto.CreateOrganisationRequestDto;
import hng_java_boilerplate.organisation.dto.CreateOrganisationResponseDto;
import hng_java_boilerplate.organisation.dto.CreatePermissionRequestDto;
import hng_java_boilerplate.organisation.dto.CreatePermissionResponseDto;
import hng_java_boilerplate.organisation.service.OrgPermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organisation")
public class OrgPermissionController {
    private final OrgPermissionService orgPermissionService;

    @PostMapping("/{orgId}/permissions")
    public ResponseEntity<CreatePermissionResponseDto> createPermission(
            @RequestBody @Valid CreatePermissionRequestDto createPermissionRequestDto,
            @PathVariable(name = "orgId") String orgId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                orgPermissionService.createPermission(createPermissionRequestDto, orgId)
        );
    }
}
