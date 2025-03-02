package hng_java_boilerplate.profile.controller;

import hng_java_boilerplate.exception.UnAuthorizedException;
import hng_java_boilerplate.profile.dto.request.DeactivateUserRequest;
import hng_java_boilerplate.profile.dto.request.UpdateUserProfileDto;
import hng_java_boilerplate.profile.dto.response.DeactivateUserResponse;
import hng_java_boilerplate.profile.dto.response.ProfileResponse;
import hng_java_boilerplate.profile.service.ProfileService;
import hng_java_boilerplate.user.dto.response.OAuthBaseResponse;
import hng_java_boilerplate.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "User Profile Management", description = "APIs for managing user profiles")
@CrossOrigin("*")
@Slf4j
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
    @ApiResponse(responseCode = "200", description = "successfully deactivates a user",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeactivateUserResponse.class))
    )
    @Operation(summary = "deactivates a user")
    public ResponseEntity<DeactivateUserResponse> deactivateUser(@RequestBody @Valid DeactivateUserRequest request) {
        return ResponseEntity.ok(profileService.deactivateUser(request));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Retrieve the profile of a user using the id of the user")
    @ApiResponse(responseCode = "200", description = "successfully retrieve user profile",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResponse.class))
    )
    public ResponseEntity<ProfileResponse> getUserProfile(@PathVariable String userId) {
        return ResponseEntity.ok(profileService.getUserProfile(userId));
    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> updateProfilePicture(@RequestParam("image") MultipartFile file) throws IOException {
        log.info("New Profile Image received");
       return profileService.uploadProfileImage(file);
    }
}
