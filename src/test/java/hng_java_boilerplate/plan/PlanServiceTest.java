package hng_java_boilerplate.plan;

import hng_java_boilerplate.plans.Plan;
import hng_java_boilerplate.plans.PlanRepository;
import hng_java_boilerplate.plans.PlanService;
import hng_java_boilerplate.plans.dtos.CreatePlanDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private PlanService planService;

    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(PlanService.class);
    }

    @Test
    public void createPlan() {
        when(this.planRepository.save(Mockito.any(Plan.class))).thenReturn(Plan.builder()
                .id(1L)
                .name("plan name")
                .description("plan description")
                .price(19.99)
                .duration_unit("day")
                .duration(1)
                .build()
        );
    }
}
