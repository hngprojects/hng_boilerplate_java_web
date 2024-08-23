package hng_java_boilerplate.pricing.plans.controller;

import hng_java_boilerplate.pricing.plans.dtos.CreatePlanDto;
import hng_java_boilerplate.pricing.plans.dtos.PlanResponse;
import hng_java_boilerplate.pricing.plans.entity.Plan;
import hng_java_boilerplate.pricing.plans.service.PlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/payment/plans")
@RequiredArgsConstructor
@Tag(name = "Plans")
public class PlanController {

    private final PlanService planService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PlanResponse> createPlan(@RequestBody @Valid CreatePlanDto createPlanDto) {
        return planService.create(createPlanDto);
    }

    @GetMapping
    public ResponseEntity<List<Plan>> getAll() {
        return planService.findAll();
    }
}
