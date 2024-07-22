package hng_java_boilerplate.plan;

import hng_java_boilerplate.plans.Plan;
import hng_java_boilerplate.plans.PlanController;
import hng_java_boilerplate.plans.PlanService;
import hng_java_boilerplate.plans.dtos.CreatePlanDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlanController.class)
public class PlanControllerTest {
    @MockBean
    private PlanService planService;

    @Autowired
    protected MockMvc mockMvc;

    private final Plan plan = Plan.builder()
            .id(UUID.randomUUID())
            .name("plan name")
            .description("plan description")
            .price(19.99)
            .duration_unit("day")
            .duration(1)
            .build();


    @Test
    public void unauthorizedRequest() throws Exception {
        Map<String, Object> response = new HashMap<>(){{
            put("status_code", 201);
            put("data", plan);
        }};
        when(planService.create(Mockito.any(CreatePlanDto.class))).thenReturn(ResponseEntity.status(201).body(response));
        this.mockMvc.perform(
                post("/api/v1/plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .header("Authorization", "")
        ).andExpect(status().isUnauthorized());
    }

}
