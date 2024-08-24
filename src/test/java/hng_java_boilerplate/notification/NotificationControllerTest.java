package hng_java_boilerplate.notification;

import hng_java_boilerplate.notification.controllers.NotificationController;
import hng_java_boilerplate.notification.dto.request.MarkRead;
import hng_java_boilerplate.notification.dto.response.NotificationData;
import hng_java_boilerplate.notification.dto.response.NotificationDto;
import hng_java_boilerplate.notification.dto.response.NotificationDtoRes;
import hng_java_boilerplate.notification.dto.response.NotificationResponse;
import hng_java_boilerplate.notification.services.NotificationService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import hng_java_boilerplate.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class NotificationControllerTest {

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private UserService userService;
    @InjectMocks
    private NotificationController notificationController;

    @MockBean
    private JwtUtils jwtUtils;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Set up mock UserService behavior
        String mockUserId = "mock-user-id";
        User mockUser = new User();
        mockUser.setId(mockUserId);
        when(userService.getLoggedInUser()).thenReturn(mockUser);
    }

    @Test
    void shouldCreateMockMvc() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    public void testGetAllNotifications() throws Exception {
        NotificationDto dto = NotificationDto.builder()
                .message("Test")
                .build();

        NotificationData data = NotificationData.builder()
                .notifications(List.of(dto))
                .total_notification_count(1)
                .total_unread_notification_count(1)
                .build();

        NotificationResponse response = NotificationResponse.builder()
                .status("success")
                .message("Notifications retrieved successfully")
                .status_code(200)
                .data(data)
                .build();

        when(notificationService.getAllNotifications()).thenReturn(response);

        mockMvc.perform(get("/api/v1/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Notifications retrieved successfully"))
                .andExpect(jsonPath("$.data.total_notification_count").value(1))
                .andExpect(jsonPath("$.data.total_unread_notification_count").value(1))
                .andExpect(jsonPath("$.data.notifications[0].message").value("Test"));
    }


    @Test
    public void testCreateNotification() throws Exception {
        NotificationDto dto = NotificationDto.builder()
                .message("new message")
                .notification_id(UUID.randomUUID())
                .build();

        NotificationData data = NotificationData.builder()
                .notifications(List.of(dto)).build();

        NotificationResponse response = NotificationResponse.builder()
                .status("success")
                .message("Notification created successfully")
                .status_code(201)
                .data(data)
                .build();

        when(notificationService.createNotification(anyString())).thenReturn(response);

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"New message\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Notification created successfully"))
                .andExpect(jsonPath("$.data.notifications[0].message").value("new message"))
                .andExpect(jsonPath("$.data.notifications[0].notification_id").value(dto.getNotification_id().toString()));
    }

    @Test
    public void testMarkAsRead() throws Exception {
        UUID notificationId = UUID.randomUUID();
        NotificationDto dto = NotificationDto.builder()
                .message("Test message")
                .build();

        NotificationDtoRes response = NotificationDtoRes.builder()
                .message("success")
                .status_code(200)
                .data(dto)
                .build();

        when(notificationService.markAsRead(eq(notificationId), any(MarkRead.class))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/notifications/{notificationId}", notificationId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"is_read\":true}")) // Ensure JSON body matches `MarkRead`
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.message").value("Test message"))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    public void testMarkAllAsRead() throws Exception {
        NotificationDto dto = NotificationDto.builder().build();

        NotificationData data = NotificationData.builder()
                .notifications(List.of(dto)).build();

        NotificationResponse response = NotificationResponse.builder()
                .message("Notification cleared successfully")
                .status("success")
                .status_code(200)
                .data(data)
                .build();

        when(notificationService.markAllAsRead()).thenReturn(response);

        mockMvc.perform(delete("/api/v1/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Notification cleared successfully"));
    }
}
