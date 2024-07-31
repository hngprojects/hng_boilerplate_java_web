package hng_java_boilerplate.profile.service;

import hng_java_boilerplate.profile.dto.request.DeactivateUserRequest;
import hng_java_boilerplate.profile.dto.response.DeactivateUserResponse;
import hng_java_boilerplate.profile.dto.response.ProfileResponse;

public interface ProfileService {
    public DeactivateUserResponse deactivateUser(DeactivateUserRequest request);

    ProfileResponse findByUserId(String userId);
}
