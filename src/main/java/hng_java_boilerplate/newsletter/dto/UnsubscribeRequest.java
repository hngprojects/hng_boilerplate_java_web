package hng_java_boilerplate.newsletter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UnsubscribeRequest(
        @Email(message="email must be a valid email")
        @NotBlank(message = "email cannot be blank")
        String email
)
{

}