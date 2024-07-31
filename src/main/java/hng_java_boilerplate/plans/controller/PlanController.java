package hng_java_boilerplate.plans.controller;

import hng_java_boilerplate.plans.dtos.CreatePlanDto;
import hng_java_boilerplate.plans.dtos.PlanObjectResponse;
import hng_java_boilerplate.plans.dtos.PlanResponse;
import hng_java_boilerplate.plans.entity.Plan;
import hng_java_boilerplate.plans.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PlanResponse> createPlan(@RequestBody @Valid CreatePlanDto createPlanDto) {
        return planService.create(createPlanDto);
    }

    @GetMapping("/billing-plans")
    public ResponseEntity<?> getAll() {
        PlanObjectResponse<?> response = planService.findAll();
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(response);
    }

    @GetMapping("/billing-plans/{planId}")
    public ResponseEntity<?> getSinglePlan(@PathVariable String planId) {
        PlanObjectResponse<?> response = planService.getSinglePlan(planId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
