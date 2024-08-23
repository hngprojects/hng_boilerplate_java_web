package hng_java_boilerplate.pricing.plans.serviceImpl;

import hng_java_boilerplate.exception.exception_class.ConflictException;
import hng_java_boilerplate.exception.exception_class.NotFoundException;
import hng_java_boilerplate.pricing.plans.dtos.CreatePlanDto;
import hng_java_boilerplate.pricing.plans.dtos.PlanResponse;
import hng_java_boilerplate.pricing.plans.entity.Plan;
import hng_java_boilerplate.pricing.plans.repository.PlanRepository;
import hng_java_boilerplate.pricing.plans.service.PlanService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
            throw new ConflictException("Plan already exists.");
        }
        Plan newPlan = Plan.builder()
                .id(UUID.randomUUID().toString())
                .price(createPlanDto.price())
                .name(createPlanDto.name())
                .description(createPlanDto.description())
                .features(createPlanDto.features())
                .build();

        Plan saved = planRepository.save(newPlan);
        return ResponseEntity.status(201).body(new PlanResponse(saved, 201, "Plan created successfully"));
    }

    @Override
    public ResponseEntity<List<Plan>> findAll() {
        List<Plan> plans = planRepository.findAll();
        return ResponseEntity.status(200).body(plans);
    }

    @Override
    public Plan findOne(String id) {
        Optional<Plan> plan = planRepository.findById(id);
        if (plan.isEmpty()) {
            throw new NotFoundException("Plan not found");
        }
        return plan.get();
    }
}
