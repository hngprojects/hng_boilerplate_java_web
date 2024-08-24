package hng_java_boilerplate.activitylog;

import hng_java_boilerplate.activitylog.controller.ActivityLogController;
import hng_java_boilerplate.activitylog.model.ActivityLog;
import hng_java_boilerplate.activitylog.service.ActivityLogService;
import hng_java_boilerplate.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ActivityLogController.class)
public class ActivityLogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityLogService activityLogService;

    @MockBean
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetActivityLogsSuccess() throws Exception {
        ActivityLog log1 = new ActivityLog();
        log1.setActivity("Login");
        log1.setTimestamp(Instant.now());

        ActivityLog log2 = new ActivityLog();
        log2.setActivity("Logout");
        log2.setTimestamp(Instant.now());

        when(activityLogService.getActivityLogs("org1", "user1")).thenReturn(Arrays.asList(log1, log2));

        mockMvc.perform(get("/api/v1/organizations/org1/users/user1/activity-logs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Activity logs retrieved successfully\",\"success\":true,\"status_code\":200,\"data\":[{\"activity\":\"Login\",\"timestamp\":\"" + log1.getTimestamp() + "\"},{\"activity\":\"Logout\",\"timestamp\":\"" + log2.getTimestamp() + "\"}]}"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetActivityLogsNotFound() throws Exception {
        when(activityLogService.getActivityLogs("org1", "user1")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/organizations/org1/users/user1/activity-logs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"status_code\":404,\"message\":\"Organization or user not found\",\"error\":\"Not Found\"}"));
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetActivityLogsServerError() throws Exception {
        when(activityLogService.getActivityLogs("org1", "user1")).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/v1/organizations/org1/users/user1/activity-logs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"status_code\":500,\"message\":\"A server error occurred\",\"error\":\"Internal Server Error\"}"));
    }
}
