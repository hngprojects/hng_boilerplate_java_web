package hng_java_boilerplate.privacy_policy.controller;

import hng_java_boilerplate.exception.ErrorResponseDto;
import hng_java_boilerplate.exception.ValidationError;
import hng_java_boilerplate.privacy_policy.dto.ApiResponse;
import hng_java_boilerplate.privacy_policy.entity.PrivacyPolicy;
import hng_java_boilerplate.privacy_policy.service.PrivacyPolicyService;
import hng_java_boilerplate.region.dto.response.GetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/privacy-policy")
@Tag(name = "Privacy Policy", description = "Controller for Privacy Policy")
public class PrivacyPolicyController {

    private final PrivacyPolicyService privacyPolicyService;

    @PostMapping
    @Secured("ROLE_SUPER_ADMIN")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "create a region success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "user not admin",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @Operation(summary = "create privacy policy", description = "Creates a region for the logged in user")
    public ResponseEntity<?> createPrivacyPolicy(@RequestBody PrivacyPolicy newPolicy) {
        PrivacyPolicy createdPolicy = privacyPolicyService.createPrivacyPolicy(newPolicy);
        return new ResponseEntity<>(new ApiResponse(
                "201",
                "Privacy policy created successfully.",
                createdPolicy
        ), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all privacy policies")
    public ResponseEntity<?> getAllPrivacyPolicies() {
        List<PrivacyPolicy> policies = privacyPolicyService.getAllPrivacyPolicies();
        return ResponseEntity.ok(new ApiResponse(
                "200",
                "Privacy policies retrieved successfully.",
                policies
        ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get a privacy policy using id of the policy")
    public ResponseEntity<?> getPrivacyPolicyById(@PathVariable UUID id) {
        PrivacyPolicy policy = privacyPolicyService.getPrivacyPolicyById(id);
        return ResponseEntity.ok(new ApiResponse(
                "200",
                "Privacy policy retrieved successfully.",
                policy
        ));
    }

    @PutMapping("/{id}")
    @Secured("ROLE_SUPER_ADMIN")
    @Operation(summary = "update a privacy policy", description = "update a privacy policy using the policy id. Only admin can update")
    public ResponseEntity<?> updatePrivacyPolicy(
            @PathVariable UUID id,
            @RequestBody PrivacyPolicy updatedPolicy) {

        PrivacyPolicy updated = privacyPolicyService.updatePrivacyPolicy(id, updatedPolicy);
        return ResponseEntity.ok(new ApiResponse(
                "200",
                "Privacy policy updated successfully.",
                updated
        ));
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_SUPER_ADMIN")
    @Operation(summary = "delete a privacy policy by id", description = "user with admin privilege can delete a policy using the policy id")
    public ResponseEntity<?> deletePrivacyPolicy(@PathVariable UUID id) {
        privacyPolicyService.deletePrivacyPolicy(id);
        return ResponseEntity.ok(new ApiResponse(
                "200",
                "Privacy policy deleted successfully.",
                null
        ));
    }


}

