package hng_java_boilerplate.pricing.plans.service;

import hng_java_boilerplate.pricing.plans.dtos.CreatePlanDto;
import hng_java_boilerplate.pricing.plans.dtos.PlanResponse;
import hng_java_boilerplate.pricing.plans.entity.Plan;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface PlanService {
    ResponseEntity<PlanResponse> create(CreatePlanDto createPlanDto);

    ResponseEntity<List<Plan>> findAll();

    Plan findOne(String id);
}
