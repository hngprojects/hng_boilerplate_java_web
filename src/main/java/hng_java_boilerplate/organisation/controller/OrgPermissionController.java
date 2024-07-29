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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/permissions")
public class OrgPermissionController {
    private final OrgPermissionService orgPermissionService;

    @PostMapping
    public ResponseEntity<CreatePermissionResponseDto> createPermission(
            @RequestBody @Valid CreatePermissionRequestDto createPermissionRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                orgPermissionService.createPermission(createPermissionRequestDto)
        );
    }
}
