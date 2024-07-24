package hng_java_boilerplate.plans.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreatePlanDto(
        @NotBlank(message = "Name can not be blank")
        String name,
        @NotBlank(message = "Description can not be blank")
        String description,
        @NotBlank(message = "Price can not be blank")
        double price,
        @NotBlank(message = "Duration can not be blank")
        int duration,
        @NotBlank(message = "Duration unit can not be blank")
        String durationUnit
) {
}
