package hng_java_boilerplate.plan_test;

import hng_java_boilerplate.plans.entity.Plan;
import hng_java_boilerplate.plans.repository.PlanRepository;
import hng_java_boilerplate.plans.serviceImpl.PlanServiceImpl;
import hng_java_boilerplate.plans.dtos.CreatePlanDto;
import hng_java_boilerplate.plans.dtos.PlanResponse;
import hng_java_boilerplate.plans.exceptions.DuplicatePlanException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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
        CreatePlanDto planDto = new CreatePlanDto("plan name", "plan description", 19.99, 1, "day");
        Plan plan = Plan.builder()
                .id(UUID.randomUUID().toString())
                .name("plan name")
                .description("plan description")
                .price(19.99)
                .durationUnit("day")
                .duration(1)
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
        assertEquals(plan.getDuration(), 1);
        assertEquals(plan.getDurationUnit(), "day");

    }

    @Test
    public void duplicatePlan() {
        CreatePlanDto planDto = new CreatePlanDto("plan name", "plan description", 19.99, 1, "day");
        when(this.planRepository.existsByName(any())).thenReturn(true);
        DuplicatePlanException exception = assertThrows(DuplicatePlanException.class, () -> planService.create(planDto));
        assertEquals("Plan already exists.", exception.getMessage());
    }
}
