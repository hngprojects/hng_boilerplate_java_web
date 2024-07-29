package hng_java_boilerplate.plans.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import hng_java_boilerplate.plans.enums.Category;
import hng_java_boilerplate.plans.enums.MembershipType;
import hng_java_boilerplate.plans.util.ValidList;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreatePlanDto(
        @NotBlank(message = "Name can not be blank")
        String name,
        @NotBlank(message = "Description can not be blank")
        String description,
        @Min(value = 1, message = "Price must be up to 1")
        double price,
        @Min(value = 1, message = "Duration must be up to 1")
        int duration,
        @JsonProperty("duration_unit")
        @NotBlank(message = "Duration unit can not be blank")
        String durationUnit,
        @ValidList
        @NotNull
        List<String> features,
        @JsonProperty("membership_type")

        MembershipType membershipType,
        Category category
) {
}
