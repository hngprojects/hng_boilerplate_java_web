package hng_java_boilerplate.organisation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateOrganisationRequestDto (
        @NotBlank(message = "Organisation Name is required")
        String name,

        @NotBlank(message = "Organisation Description is required")
        String description,

        @Email(message = "Organisation Email is not properly formatted")
        @NotBlank(message = "Organisation Email is required")
        String email,

        @NotBlank(message = "Organisation Industry is required")
        String industry,

        @NotBlank(message = "Organisation Type is required")
        String type,

        @NotBlank(message = "Organisation Country is required")
        String country,

        @NotBlank(message = "Organisation Address is required")
        String address,

        @NotBlank(message = "Organisation State is required")
        String state
){
}