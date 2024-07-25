package hng_java_boilerplate.profile.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeactivateUserRequest {
    private String reason;
    @NotBlank(message = "Deactivation confirmation is required")
    private String confirmation;
}
