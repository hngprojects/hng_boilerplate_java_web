package hng_java_boilerplate.plan;

import hng_java_boilerplate.plans.Plan;
import hng_java_boilerplate.plans.PlanRepository;
import hng_java_boilerplate.plans.PlanService;
import hng_java_boilerplate.plans.dtos.CreatePlanDto;
import hng_java_boilerplate.plans.exceptions.DuplicatePlanException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private PlanService planService;

    private Plan plan;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        plan = Plan.builder()
                .id(UUID.randomUUID())
                .name("plan name")
                .description("plan description")
                .price(19.99)
                .duration_unit("day")
                .duration(1)
                .build();
    }

    @Test
    public void createPlan() {
        CreatePlanDto planDto = new CreatePlanDto("plan name", "plan description", 19.99, 1, "day");
        when(this.planRepository.save(Mockito.any(Plan.class))).thenReturn(plan);
        ResponseEntity<Object> objectResponseEntity = planService.create(planDto);
        assertNotNull(objectResponseEntity);
        assertEquals(objectResponseEntity.getStatusCode().value(), 201);

    }

    @Test
    public void duplicatePlan() {
        when(this.planRepository.existsByName("Duplicate plan")).thenReturn(true);
        DuplicatePlanException exception = assertThrows(DuplicatePlanException.class, () -> planRepository.save(plan));
        assertEquals("Plan already exists.", exception.getMessage());
    }
}
