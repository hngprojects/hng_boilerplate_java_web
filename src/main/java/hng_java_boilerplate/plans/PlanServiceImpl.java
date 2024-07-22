package hng_java_boilerplate.plans;

import hng_java_boilerplate.plans.dtos.CreatePlanDto;
import hng_java_boilerplate.plans.exceptions.DuplicatePlanException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

    @Transactional
    @Override
    public ResponseEntity<Object> create(CreatePlanDto createPlanDto) {
        boolean planExists = planRepository.existsByName(createPlanDto.name());

        if (planExists) {
            throw new DuplicatePlanException("Plan already exists.");
        }
        Plan newPlan = Plan.builder()
                .price(createPlanDto.price())
                .name(createPlanDto.name())
                .description(createPlanDto.description())
                .duration(createPlanDto.duration())
                .duration_unit(createPlanDto.duration_unit())
                .build();

        Plan saved = planRepository.save(newPlan);
        return ResponseEntity.status(201).body(new HashMap<>() {{
            put("data", saved);
            put("status_code", 201);
        }});
    }
}
