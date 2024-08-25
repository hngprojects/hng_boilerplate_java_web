package hng_java_boilerplate.statusPage.controller;

import hng_java_boilerplate.statusPage.entity.StatusPage;
import hng_java_boilerplate.statusPage.service.StatusPageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StatusPageControllerTest {

    @Mock
    private StatusPageService statusPageService;

    @InjectMocks
    private StatusPageController statusPageController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(statusPageController).build();
    }

    @Test
    void testGetAllApiStatuses() throws Exception {
        // Prepare test data
        List<StatusPage> mockStatusPages = Arrays.asList(new StatusPage(), new StatusPage());
        when(statusPageService.getAllApiStatuses()).thenReturn(mockStatusPages);

        // Perform GET request and verify the response
        mockMvc.perform(get("/api/v1/api-status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(mockStatusPages.size()));

        // Verify that the service method was called
        verify(statusPageService, times(1)).getAllApiStatuses();
    }

    @Test
    void testUpdateApiStatus() throws Exception {
        // Perform POST request and verify the response
        mockMvc.perform(post("/api/v1/api-status")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify that the service method was called
        verify(statusPageService, times(1)).updateApiStatus();
    }
}