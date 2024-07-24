package hng_java_boilerplate.plans.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreatePlanDto(
        @NotBlank
        String name,
        @NotBlank
        String description,
        @NotBlank
        double price,
        @NotBlank
        int duration,
        @NotBlank
        String duration_unit
) {
}
