package hng_java_boilerplate.authentication.twofactor.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record TotpRequest(
        @JsonProperty("totp_code")
        @NotBlank(message = "Totp code can not be blank")
        String totp
) {
}
