package hng_java_boilerplate.plans;

import hng_java_boilerplate.plans.dtos.CreatePlanDto;
import hng_java_boilerplate.plans.dtos.PlanResponse;
import org.springframework.http.ResponseEntity;

public interface PlanService {
    ResponseEntity<PlanResponse> create(CreatePlanDto createPlanDto);
}
