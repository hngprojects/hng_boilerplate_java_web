package hng_java_boilerplate.profile.dto.response;

import hng_java_boilerplate.profile.entity.Profile;

public record ProfileResponse(int status_code, String message, ProfileDto data) {
}
