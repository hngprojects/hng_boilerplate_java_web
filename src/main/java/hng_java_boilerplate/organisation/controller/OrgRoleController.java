package hng_java_boilerplate.organisation.controller;

import hng_java_boilerplate.organisation.dto.CreateOrganisationRequestDto;
import hng_java_boilerplate.organisation.dto.CreateOrganisationResponseDto;
import hng_java_boilerplate.organisation.dto.CreateRoleRequestDto;
import hng_java_boilerplate.organisation.dto.CreateRoleResponseDto;
import hng_java_boilerplate.organisation.service.OrgRoleService;
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
@RequestMapping("/api/v1/roles")
public class OrgRoleController {
    private final OrgRoleService orgRoleService;

    @PostMapping
    public ResponseEntity<CreateRoleResponseDto> createOrganisation(
            @RequestBody @Valid CreateRoleRequestDto createRoleRequestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                orgRoleService.createRole(createRoleRequestDto)
        );
    }
}
