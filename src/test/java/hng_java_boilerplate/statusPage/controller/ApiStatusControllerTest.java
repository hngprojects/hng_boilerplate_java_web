package hng_java_boilerplate.statusPage.controller;

import hng_java_boilerplate.statusPage.entity.ApiStatus;
import hng_java_boilerplate.statusPage.service.ApiStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

public class ApiStatusControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ApiStatusService apiStatusService;

    @InjectMocks
    private ApiStatusController apiStatusController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(apiStatusController).build();
    }

    @Test
    public void testGetAllApiStatuses() throws Exception {
        // Arrange
        ApiStatus apiStatus = new ApiStatus();
        apiStatus.setApiGroup("Test API");
        apiStatus.setStatus(ApiStatus.Status.OPERATIONAL);
        apiStatus.setLastChecked(LocalDateTime.now());

        List<ApiStatus> apiStatuses = Collections.singletonList(apiStatus);

        when(apiStatusService.getAllApiStatuses()).thenReturn(apiStatuses);

        // Act and Assert
        mockMvc.perform(get("/api/v1/api-status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].apiGroup", is("Test API")))
                .andExpect(jsonPath("$[0].status", is("OPERATIONAL")));
    }

    @Test
    public void testUpdateApiStatus() throws Exception {
        // Arrange
        ApiStatus apiStatus = new ApiStatus();
        apiStatus.setApiGroup("Test API");
        apiStatus.setStatus(ApiStatus.Status.OPERATIONAL);
        apiStatus.setLastChecked(LocalDateTime.now());

        when(apiStatusService.updateApiStatus(any(ApiStatus.class))).thenReturn(apiStatus);

        // Act and Assert
        mockMvc.perform(post("/api/v1/api-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"apiGroup\":\"Test API\",\"status\":\"OPERATIONAL\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.apiGroup", is("Test API")))
                .andExpect(jsonPath("$.status", is("OPERATIONAL")));
    }
}
