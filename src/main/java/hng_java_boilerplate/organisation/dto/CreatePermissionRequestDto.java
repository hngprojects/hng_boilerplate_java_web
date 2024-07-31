package hng_java_boilerplate.organisation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record CreatePermissionRequestDto(
        @NotBlank(message = "Permission name is required")
        String name,
        @NotBlank(message = "Permission description is required")
        String description
) {
}
