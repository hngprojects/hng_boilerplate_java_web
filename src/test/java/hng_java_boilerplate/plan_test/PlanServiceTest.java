package hng_java_boilerplate.plan_test;

import hng_java_boilerplate.exception.exception_class.ConflictException;
import hng_java_boilerplate.pricing.plans.dtos.CreatePlanDto;
import hng_java_boilerplate.pricing.plans.dtos.PlanResponse;
import hng_java_boilerplate.pricing.plans.entity.Plan;
import hng_java_boilerplate.pricing.plans.repository.PlanRepository;
import hng_java_boilerplate.pricing.plans.serviceImpl.PlanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


public class PlanServiceTest {

    @InjectMocks
    private PlanServiceImpl planService;
    @Mock
    private PlanRepository planRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void createPlan() {
        List<String> features = new ArrayList<>() {{
            add("Feature 1");
            add("Feature 2");
        }};
        CreatePlanDto planDto = new CreatePlanDto("plan name", "plan description", 19.99, 1, "day", features);
        Plan plan = Plan.builder()
                .id(UUID.randomUUID().toString())
                .name("plan name")
                .description("plan description")
                .price(19.99)
                .features(features)
                .build();
        when(this.planRepository.save(any(Plan.class))).thenReturn(plan);
        when(this.planRepository.existsByName("plan name")).thenReturn(false);
        ResponseEntity<PlanResponse> response = planService.create(planDto);


        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);

        PlanResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.CREATED.value(), body.statusCode());
        assertEquals("Plan created successfully", body.message());

        var data = body.data();
        assertNotNull(plan);
        assertNotNull(plan.getId());
        assertEquals(plan.getDescription(), "plan description");
        assertEquals(plan.getName(), "plan name");
        assertEquals(plan.getPrice(), 19.99);
        assertNotNull(plan.getFeatures());

    }

    @Test
    public void duplicatePlan() {
        List<String> features = new ArrayList<>() {{
            add("Feature 1");
            add("Feature 2");
        }};
        CreatePlanDto planDto = new CreatePlanDto("plan name", "plan description", 19.99, 1, "day", features);
        when(this.planRepository.existsByName(any())).thenReturn(true);
        ConflictException exception = assertThrows(ConflictException.class, () -> planService.create(planDto));
        assertEquals("Plan already exists.", exception.getMessage());
    }

    @Test
    public void getAllPlans() {
        List<String> features = new ArrayList<>() {{
            add("Feature 1");
            add("Feature 2");
        }};
        Plan plan1 = Plan.builder()
                .id(UUID.randomUUID().toString())
                .name("plan name")
                .description("plan description")
                .price(19.99)
                .features(features)
                .build();
        Plan plan2 = Plan.builder()
                .id(UUID.randomUUID().toString())
                .name("plan name")
                .description("plan description")
                .price(19.99)
                .features(features)
                .build();
        List<Plan> plans = List.of(plan1, plan2);
        when(this.planRepository.findAll()).thenReturn(plans);
        ResponseEntity<List<Plan>> response = planService.findAll();

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        List<Plan> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains(plan1));
        assertTrue(body.contains(plan2));

    }
}
