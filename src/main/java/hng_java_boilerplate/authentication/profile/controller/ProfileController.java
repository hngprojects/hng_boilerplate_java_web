package hng_java_boilerplate.authentication.profile.controller;

import hng_java_boilerplate.authentication.profile.dto.request.DeactivateUserRequest;
import hng_java_boilerplate.authentication.profile.dto.response.DeactivateUserResponse;
import hng_java_boilerplate.authentication.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="Profiles")
public class ProfileController {
    private final ProfileService profileService;

    @PatchMapping("/deactivate")
    public ResponseEntity<DeactivateUserResponse> deactivateUser(@RequestBody @Valid DeactivateUserRequest request) {
        return ResponseEntity.ok(profileService.deactivateUser(request));
    }
}
