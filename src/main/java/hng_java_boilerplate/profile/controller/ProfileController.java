package hng_java_boilerplate.profile.controller;

import hng_java_boilerplate.profile.dto.request.DeactivateUserRequest;
import hng_java_boilerplate.profile.dto.response.DeactivateUserResponse;
import hng_java_boilerplate.profile.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class ProfileController {
    private final ProfileService profileService;

    @PatchMapping("/deactivate")
    public ResponseEntity<DeactivateUserResponse> deactivateUser(@RequestBody @Valid DeactivateUserRequest request) {
        return ResponseEntity.ok(profileService.deactivateUser(request));
    }
}
