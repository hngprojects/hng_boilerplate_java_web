package hng_java_boilerplate.plans.service;

import hng_java_boilerplate.plans.dtos.AllPlansDto;
import hng_java_boilerplate.plans.dtos.CreatePlanDto;
import hng_java_boilerplate.plans.dtos.PlanObjectResponse;
import hng_java_boilerplate.plans.dtos.PlanResponse;
import hng_java_boilerplate.plans.entity.Plan;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface PlanService {

    ResponseEntity<PlanResponse> create(CreatePlanDto createPlanDto);
    ResponseEntity<AllPlansDto> findAll();

    Optional<Plan> findOne(String id);
    ResponseEntity<PlanResponse> getPlan(String id);

}
