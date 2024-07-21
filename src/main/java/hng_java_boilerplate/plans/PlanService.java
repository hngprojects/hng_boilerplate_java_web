package hng_java_boilerplate.plans;

import hng_java_boilerplate.plans.dtos.CreatePlanDto;
import org.springframework.http.ResponseEntity;

public interface PlanService {

    ResponseEntity<Object> create(CreatePlanDto createPlanDto);
}
