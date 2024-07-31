package hng_java_boilerplate.notification;

// other imports...

import hng_java_boilerplate.notification.controllers.NotificationController;
import hng_java_boilerplate.notification.models.Notification;
import hng_java_boilerplate.notification.services.NotificationService;
import hng_java_boilerplate.product.errorhandler.ProductErrorHandler;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private NotificationController notificationController;
    @MockBean
    private ProductErrorHandler productErrorHandler;


    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();

        // Set up mock UserService behavior
        String mockUserId = "mock-user-id";
        User mockUser = new User();
        mockUser.setId(mockUserId);
        when(userService.getLoggedInUser()).thenReturn(mockUser);
    }

    @Test
    public void testGetAllNotifications() throws Exception {
        List<Notification> notifications = new ArrayList<>();
        Notification notification = new Notification();
        notification.setMessage("Test message");
        notifications.add(notification);

        when(notificationService.getAllNotifications()).thenReturn(notifications);
        when(notificationService.getTotalUnreadNotificationCount()).thenReturn(1);

        mockMvc.perform(get("/api/v1/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Notifications retrieved successfully"))
                .andExpect(jsonPath("$.data.total_notification_count").value(1))
                .andExpect(jsonPath("$.data.total_unread_notification_count").value(1))
                .andExpect(jsonPath("$.data.notifications[0].message").value("Test message"));
    }

    @Test
    public void testCreateNotification() throws Exception {
        Notification notification = new Notification();
        notification.setMessage("New message");
        notification.setUserId("mock-user-id");

        when(notificationService.createNotification(anyString())).thenReturn(notification);

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"New message\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Notification created successfully"))
                .andExpect(jsonPath("$.data.notifications[0].message").value("New message"));
    }

    @Test
    public void testMarkAsRead() throws Exception {
        UUID notificationId = UUID.randomUUID();
        Notification notification = new Notification();
        notification.setNotificationId(notificationId);
        notification.setIsRead(true);

        when(notificationService.markAsRead(notificationId)).thenReturn(notification);

        mockMvc.perform(patch("/api/v1/notifications/{notificationId}", notificationId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notificationId").value(notificationId.toString()))
                .andExpect(jsonPath("$.isRead").value(true));
    }

    @Test
    public void testMarkAllAsRead() throws Exception {
        List<Notification> notifications = new ArrayList<>();
        Notification notification = new Notification();
        notification.setIsRead(true);
        notifications.add(notification);

        when(notificationService.markAllAsRead()).thenReturn(notifications);

        mockMvc.perform(delete("/api/v1/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Notifications cleared successfully"))
                .andExpect(jsonPath("$.data.notifications").isEmpty());
    }
}
