package hng_java_boilerplate.plans.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import hng_java_boilerplate.plans.entity.Plan;

import java.util.List;

public record AllPlansDto(
        @JsonProperty("status")
        int statusCode,
        String message,
        List<Plan> data
) {
}