package hng_java_boilerplate.plans;

import hng_java_boilerplate.plans.dtos.CreatePlanDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

    @Transactional
    @Override
    public ResponseEntity<Object> create(CreatePlanDto createPlanDto) {
        Optional<Plan> plan = planRepository.findByName(createPlanDto.name());

        if (plan.isPresent()) {
            return ResponseEntity.status(400).body(new HashMap<>(){{
                put("status_code", 400);
                put("error", "Subscription plan already exists");
            }});
        }
        Plan newPlan = Plan.builder()
                .price(createPlanDto.price())
                .name(createPlanDto.name())
                .description(createPlanDto.description())
                .duration(createPlanDto.duration())
                .duration_unit(createPlanDto.duration_unit())
                .build();

        Plan saved = planRepository.save(newPlan);
        return ResponseEntity.status(201).body(new HashMap<>(){{
            put("data", saved);
            put("status_code", 201);
        }});
    }
}
