package hng_java_boilerplate.profile.service;

import hng_java_boilerplate.profile.dto.request.DeactivateUserRequest;
import hng_java_boilerplate.profile.dto.response.DeactivateUserResponse;

public interface ProfileService {
    public DeactivateUserResponse deactivateUser(DeactivateUserRequest request);
}
