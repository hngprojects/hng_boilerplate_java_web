package hng_java_boilerplate.organisation.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateOrganisationRequestDto(
        @NotBlank(message = "Name must be a string")
        String name,
        @NotBlank(message = "Description must be a string")
        String description
) {
}
