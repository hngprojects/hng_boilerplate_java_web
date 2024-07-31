package hng_java_boilerplate.profile.controller;

import hng_java_boilerplate.profile.dto.response.ProfileResponse;
import hng_java_boilerplate.profile.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "User Profile Management", description = "APIs for managing user profiles")
public class UserProfileController {

    private final ProfileService profileService;

    public UserProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<ProfileResponse> getUserProfile(@PathVariable("user_id") String userId){
        return ResponseEntity.ok(profileService.findByUserId(userId));
    }
}
