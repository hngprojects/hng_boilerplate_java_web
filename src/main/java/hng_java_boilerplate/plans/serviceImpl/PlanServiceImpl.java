package hng_java_boilerplate.plans.serviceImpl;

import hng_java_boilerplate.plans.dtos.AllPlansDto;
import hng_java_boilerplate.plans.dtos.CreatePlanDto;
import hng_java_boilerplate.plans.dtos.PlanResponse;
import hng_java_boilerplate.plans.entity.Plan;
import hng_java_boilerplate.plans.exceptions.DuplicatePlanException;
import hng_java_boilerplate.plans.exceptions.PlanNotFoundException;
import hng_java_boilerplate.plans.repository.PlanRepository;
import hng_java_boilerplate.plans.service.PlanService;
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
            throw new DuplicatePlanException("Plan already exists.");
        }
        Plan newPlan = Plan.builder()
                .id(UUID.randomUUID().toString())
                .price(createPlanDto.price())
                .name(createPlanDto.name())
                .build();

        Plan saved = planRepository.save(newPlan);
        return ResponseEntity.status(201).body(new PlanResponse( 201, "Plan created successfully", saved));
    }

    @Override
    public ResponseEntity<AllPlansDto> findAll() {
        List<Plan> plans = planRepository.findAll();
        return ResponseEntity.status(200).body(new AllPlansDto(200, "Billing plans retrieved successfully", plans));
    }

    @Override
    public Optional<Plan> findOne(String id) {
        return planRepository.findById(id);
    }

    @Override
    public ResponseEntity<PlanResponse> getPlan(String id) {
        Optional<Plan> plan = findOne(id);
        if (plan.isEmpty()) {
            throw new PlanNotFoundException("Pricing Plan not found");
        }
        return ResponseEntity.status(200).body(new PlanResponse(200, "Billing plan retrieved successfully", plan.get()));
    }
}