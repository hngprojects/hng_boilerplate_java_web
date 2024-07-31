package hng_java_boilerplate.plans.serviceImpl;

import hng_java_boilerplate.plans.dtos.CreatePlanDto;
import hng_java_boilerplate.plans.dtos.PlanObjectResponse;
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

import java.util.*;
import java.util.stream.Collectors;

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
                .durationUnit(createPlanDto.durationUnit())
                .features(createPlanDto.features())
                .build();

        Plan saved = planRepository.save(newPlan);
        return ResponseEntity.status(201).body(new PlanResponse(saved, 201, "Plan created successfully"));
    }


    @Override
    public PlanObjectResponse<?> findAll() {
        List<Plan> plans = planRepository.findAll();

        List<Map<String, Object>> list = plans.stream().map(singlePlan -> {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("id", singlePlan.getId());
            data.put("name", singlePlan.getPlanType());
            data.put("price", singlePlan.getPrice());
            data.put("description", singlePlan.getDescription());
            return data;
        }).collect(Collectors.toList());

        return PlanObjectResponse.builder().message("Billing plans retrieved successfully").status("200").data(list).build();
    }

    @Override
    public PlanObjectResponse<?> getSinglePlan(String planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException("Plan not found"));
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", plan.getId());
        data.put("name", plan.getPlanType());
        data.put("price", plan.getPrice());
        data.put("description", plan.getDescription());

        return PlanObjectResponse.builder().message("Billing plan retrieved successfully").status("200").data(data).build();
    }


//    @Override
//    public PlanObjectResponse<?> findAll() {
//        List<Plan> plans = planRepository.findAll();
//        List<Object> list = new ArrayList<>();
//        for (Plan singlePlan: plans) {
//            Map<String, Object> data = new LinkedHashMap<>();
//            data.put("id", singlePlan.getId());
//            data.put("name", singlePlan.getPlanType());
//            data.put("price", singlePlan.getPrice());
//            data.put("description", singlePlan.getDescription());
//            list.add(data);
//        }
//        return PlanObjectResponse.builder().message("Billing plans retrieved successfully").status("200").data(list).build();
//    }
}
