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

    @PostMapping
    public ResponseEntity<CreateOrganisationResponseDto> createOrganisation(
            @RequestBody @Valid CreateOrganisationRequestDto orgRequest,
            Authentication activeUser
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                organisationService.create(orgRequest, activeUser)
        );
    }
}
