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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organisation")
public class OrgRoleController {
    private final OrgRoleService orgRoleService;

    @PostMapping("/{orgId}/roles")
    public ResponseEntity<CreateRoleResponseDto> createOrganisation(
            @RequestBody @Valid CreateRoleRequestDto createRoleRequestDto,
            @PathVariable(name = "orgId") String orgId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                orgRoleService.createRole(createRoleRequestDto, orgId)
        );
    }
}
