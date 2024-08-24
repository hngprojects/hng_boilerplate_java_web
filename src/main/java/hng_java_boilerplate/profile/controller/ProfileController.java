package hng_java_boilerplate.profile.controller;

import hng_java_boilerplate.exception.UnAuthorizedException;
import hng_java_boilerplate.profile.dto.request.DeactivateUserRequest;
import hng_java_boilerplate.profile.dto.request.UpdateUserProfileDto;
import hng_java_boilerplate.profile.dto.response.DeactivateUserResponse;
import hng_java_boilerplate.profile.dto.response.ProfileResponse;
import hng_java_boilerplate.profile.service.ProfileService;
import hng_java_boilerplate.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "User Profile Management", description = "APIs for managing user profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Operation(summary = "Update User Profile", description = "Updates the profile information for the specified user ID.")
    @PatchMapping("/{user_id}")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable("user_id") String user_id,
            @RequestBody UpdateUserProfileDto updateUserProfileDto,
            Authentication authentication
    ){

        User user = (User) authentication.getPrincipal();
        String userId = user.getId();
        if(!user_id.equals(userId)){
            throw new UnAuthorizedException("User not authorized");
        }
        Optional<?> updatedUserProfile = profileService.updateUserProfile(userId, updateUserProfileDto);
        return new ResponseEntity<>(updatedUserProfile, HttpStatus.OK);
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<DeactivateUserResponse> deactivateUser(@RequestBody @Valid DeactivateUserRequest request) {
        return ResponseEntity.ok(profileService.deactivateUser(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponse> getUserProfile(@PathVariable String userId) {
        return ResponseEntity.ok(profileService.getUserProfile(userId));
    }
}
