package hng_java_boilerplate.plans.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreatePlanDto(
        @NotBlank(message = "Name can not be blank")
        String name,
        @NotBlank(message = "Description can not be blank")
        String description,
        @NotBlank(message = "Price can not be blank")
        double price,
        @NotBlank(message = "Duration can not be blank")
        int duration,
        @JsonProperty("duration_unit")
        @NotBlank(message = "Duration unit can not be blank")
        String durationUnit,
        @NotBlank(message = "Features can not be blank")
        List<String> features
) {
}
