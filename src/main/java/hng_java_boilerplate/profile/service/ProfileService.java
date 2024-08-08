package hng_java_boilerplate.profile.service;

import hng_java_boilerplate.profile.dto.request.DeactivateUserRequest;
import hng_java_boilerplate.profile.dto.request.UpdateUserProfileDto;
import hng_java_boilerplate.profile.dto.response.DeactivateUserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ProfileService {
    public DeactivateUserResponse deactivateUser(DeactivateUserRequest request);
    String uploadProfileImage(MultipartFile file, String userId) throws IOException;
    Optional<?> updateUserProfile(String userId, UpdateUserProfileDto updateUserProfileDto);

}
