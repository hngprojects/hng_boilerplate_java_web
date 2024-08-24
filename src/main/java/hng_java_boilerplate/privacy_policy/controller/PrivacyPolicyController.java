package hng_java_boilerplate.privacy_policy.controller;

import hng_java_boilerplate.privacy_policy.dto.ApiResponse;
import hng_java_boilerplate.privacy_policy.entity.PrivacyPolicy;
import hng_java_boilerplate.privacy_policy.service.PrivacyPolicyService;
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
public class PrivacyPolicyController {

    private final PrivacyPolicyService privacyPolicyService;

    @PostMapping
    @Secured("ROLE_SUPER_ADMIN")
    public ResponseEntity<?> createPrivacyPolicy(@RequestBody PrivacyPolicy newPolicy) {
        PrivacyPolicy createdPolicy = privacyPolicyService.createPrivacyPolicy(newPolicy);
        return new ResponseEntity<>(new ApiResponse(
                "201",
                "Privacy policy created successfully.",
                createdPolicy
        ), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllPrivacyPolicies() {
        List<PrivacyPolicy> policies = privacyPolicyService.getAllPrivacyPolicies();
        return ResponseEntity.ok(new ApiResponse(
                "200",
                "Privacy policies retrieved successfully.",
                policies
        ));
    }

    @GetMapping("/{id}")
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
    public ResponseEntity<?> deletePrivacyPolicy(@PathVariable UUID id) {
        privacyPolicyService.deletePrivacyPolicy(id);
        return ResponseEntity.ok(new ApiResponse(
                "200",
                "Privacy policy deleted successfully.",
                null
        ));
    }


}

