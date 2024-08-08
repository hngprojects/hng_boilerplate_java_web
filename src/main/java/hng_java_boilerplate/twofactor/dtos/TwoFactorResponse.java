package hng_java_boilerplate.twofactor.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TwoFactorResponse(
        @JsonProperty("status_code")
        int statusCode,
        String message,
        Object data
) {
}
