package hng_java_boilerplate.twofactor.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TwoFactorResponse(
        @JsonProperty("status_code")
        int statusCode,
        String message,
        Object data
) {
}
