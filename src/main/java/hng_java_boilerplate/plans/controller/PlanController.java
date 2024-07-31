package hng_java_boilerplate.plans.controller;

import hng_java_boilerplate.plans.dtos.AllPlansDto;
import hng_java_boilerplate.plans.dtos.CreatePlanDto;
import hng_java_boilerplate.plans.dtos.PlanResponse;
import hng_java_boilerplate.plans.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/billing-plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PlanResponse> createPlan(@RequestBody @Valid CreatePlanDto createPlanDto) {
        return planService.create(createPlanDto);
    }

    @GetMapping
    public ResponseEntity<AllPlansDto> getAll() {
        return planService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> getPlan(@PathVariable String id) {
        return planService.getPlan(id);
    }
}