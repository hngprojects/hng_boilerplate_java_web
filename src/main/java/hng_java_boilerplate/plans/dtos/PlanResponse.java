package hng_java_boilerplate.plans.dtos;

import hng_java_boilerplate.plans.entity.Plan;

public record PlanResponse(
        Plan data,
        int statusCode,
        String message
) {
}
