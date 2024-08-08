package hng_java_boilerplate.twofactor.dtos;

import jakarta.validation.constraints.NotBlank;

public record TotpRequest(
        @NotBlank(message = "Totp code can not be blank")
        String totp
) {
}
