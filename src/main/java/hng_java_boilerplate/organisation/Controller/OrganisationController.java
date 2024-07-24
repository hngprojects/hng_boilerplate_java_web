package hng_java_boilerplate.organisation.Controller;

import hng_java_boilerplate.organisation.ServiceImpl.OrganisationService;
import hng_java_boilerplate.organisation.dto.AddUserToOrganisationRequestDto;
import hng_java_boilerplate.organisation.dto.OrganisationResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/organisations")
public class OrganisationController {
    @Autowired
    private OrganisationService organisationService;

    @PostMapping("/{orgId}/users")
    public ResponseEntity<?> addUserToOrganisation(@PathVariable String orgId, @Valid @RequestBody AddUserToOrganisationRequestDto request,
            Authentication authentication) {
        OrganisationResponseDto responseDTO = organisationService.addUserToOrganisation(orgId, request, authentication);
        return ResponseEntity.ok(responseDTO);
    }
}
