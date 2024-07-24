package hng_java_boilerplate.plans.serviceImpl;

import hng_java_boilerplate.plans.dtos.CreatePlanDto;
import hng_java_boilerplate.plans.dtos.PlanResponse;
import hng_java_boilerplate.plans.entity.Plan;
import hng_java_boilerplate.plans.exceptions.DuplicatePlanException;
import hng_java_boilerplate.plans.repository.PlanRepository;
import hng_java_boilerplate.plans.service.PlanService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

    @Transactional
    @Override
    public ResponseEntity<PlanResponse> create(CreatePlanDto createPlanDto) {
        boolean planExists = planRepository.existsByName(createPlanDto.name());

        if (planExists) {
            throw new DuplicatePlanException("Plan already exists.");
        }
        Plan newPlan = Plan.builder()
                .id(UUID.randomUUID().toString())
                .price(createPlanDto.price())
                .name(createPlanDto.name())
                .description(createPlanDto.description())
                .duration(createPlanDto.duration())
                .duration_unit(createPlanDto.duration_unit())
                .build();

        Plan saved = planRepository.save(newPlan);
        return ResponseEntity.status(201).body(new PlanResponse(saved, 201, "Plan created successfully"));
    }
}
