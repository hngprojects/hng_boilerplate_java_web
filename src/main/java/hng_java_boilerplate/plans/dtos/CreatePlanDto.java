package hng_java_boilerplate.plans.dtos;

public record CreatePlanDto(
        String name,
        String description,
        Double price,
        int duration,
        String duration_unit
) {
}
