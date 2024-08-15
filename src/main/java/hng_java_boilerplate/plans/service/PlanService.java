package hng_java_boilerplate.plans.service;

import hng_java_boilerplate.plans.dtos.CreatePlanDto;
import hng_java_boilerplate.plans.dtos.PlanResponse;
import hng_java_boilerplate.plans.entity.Plan;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface PlanService {
    ResponseEntity<PlanResponse> create(CreatePlanDto createPlanDto);

    ResponseEntity<List<Plan>> findAll();

    Plan findOne(String id);
}
