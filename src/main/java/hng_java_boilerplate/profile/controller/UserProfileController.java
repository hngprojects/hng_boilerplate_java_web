package hng_java_boilerplate.profile.controller;

import hng_java_boilerplate.profile.dto.request.UpdateUserProfileDto;
import hng_java_boilerplate.profile.dto.response.ProfileUpdateResponseDto;
import hng_java_boilerplate.profile.exceptions.InternalServerErrorException;
import hng_java_boilerplate.profile.exceptions.NotFoundException;
import hng_java_boilerplate.profile.exceptions.UnauthorizedException;
import hng_java_boilerplate.profile.service.ProfileService;
import hng_java_boilerplate.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "User Profile Management", description = "APIs for managing user profiles")
public class UserProfileController {

    private final ProfileService profileService;

    public UserProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Operation(summary = "Update User Profile", description = "Updates the profile information for the specified user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(schema = @Schema(implementation = ProfileUpdateResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "User not authorized",
                    content = @Content(schema = @Schema(implementation = UnauthorizedException.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = NotFoundException.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error",
                    content = @Content(schema = @Schema(implementation = InternalServerErrorException.class)))
    })
    @PatchMapping("/{user_id}")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable("user_id") String user_id,
            @RequestBody UpdateUserProfileDto updateUserProfileDto,
            Authentication authentication
    ){

        User user = (User) authentication.getPrincipal();
        String userId = user.getId();
        if(!user_id.equals(userId)){
            throw new UnauthorizedException("User not authorized");
        }
        Optional<?> updatedUserProfile = profileService.updateUserProfile(userId, updateUserProfileDto);
        return new ResponseEntity<>(updatedUserProfile, HttpStatus.OK);
    }
}
