package hng_java_boilerplate.profile.service;

import hng_java_boilerplate.profile.dto.request.DeactivateUserRequest;
import hng_java_boilerplate.profile.dto.request.UpdateUserProfileDto;
import hng_java_boilerplate.profile.dto.response.DeactivateUserResponse;

import java.util.Optional;

public interface ProfileService {
    public DeactivateUserResponse deactivateUser(DeactivateUserRequest request);

    Optional<?> updateUserProfile(String userId, UpdateUserProfileDto updateUserProfileDto);

}
