package hng_java_boilerplate.plans.dtos;

import hng_java_boilerplate.plans.Plan;

public record PlanResponse(
        Plan data,
        int status_code,
        String message
) {
}
