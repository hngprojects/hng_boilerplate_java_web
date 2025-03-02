package hng_java_boilerplate.profile.service;

import hng_java_boilerplate.profile.dto.request.DeactivateUserRequest;
import hng_java_boilerplate.profile.dto.request.UpdateUserProfileDto;
import hng_java_boilerplate.profile.dto.response.DeactivateUserResponse;
import hng_java_boilerplate.profile.dto.response.ProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ProfileService {
    DeactivateUserResponse deactivateUser(DeactivateUserRequest request);
    Optional<?> updateUserProfile(String userId, UpdateUserProfileDto updateUserProfileDto);
    ProfileResponse getUserProfile(String userId);
    ResponseEntity<?> uploadProfileImage(MultipartFile file) throws IOException;
}


