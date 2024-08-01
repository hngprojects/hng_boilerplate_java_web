package hng_java_boilerplate.notification;

import hng_java_boilerplate.notification.controllers.NotificationSettingsController;
import hng_java_boilerplate.notification.models.NotificationSettings;
import hng_java_boilerplate.notification.services.NotificationSettingsService;
import hng_java_boilerplate.product.errorhandler.ProductErrorHandler;
import hng_java_boilerplate.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JwtUtils.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(NotificationSettingsController.class)
public class NotificationSettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationSettingsService service;
    @MockBean
    private ProductErrorHandler productErrorHandler;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new NotificationSettingsController(service)).build();
    }

    @Test
    void testGetSettings() throws Exception {
        NotificationSettings settings = new NotificationSettings();
        // Configure your NotificationSettings object here

        when(service.getSettings()).thenReturn(settings);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Notification preferences retrieved successfully");
        response.put("status_code", 200);
        response.put("data", settings);

        mockMvc.perform(get("/api/v1/notification-settings"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":\"success\",\"message\":\"Notification preferences retrieved successfully\",\"status_code\":200,\"data\":{}}"));
    }

    @Test
    void testUpdateSettings() throws Exception {
        NotificationSettings settings = new NotificationSettings();
        // Configure your NotificationSettings object with valid data
        settings.setId(1L);
        settings.setUserId("user123");
        settings.setMobilePushNotifications(true);
        settings.setEmailNotificationActivityInWorkspace(true);
        settings.setEmailNotificationAlwaysSendEmailNotifications(false);
        settings.setEmailNotificationEmailDigest(true);
        settings.setEmailNotificationAnnouncementAndUpdateEmails(false);
        settings.setSlackNotificationsActivityOnYourWorkspace(true);
        settings.setSlackNotificationsAlwaysSendEmailNotifications(false);
        settings.setSlackNotificationsAnnouncementAndUpdateEmails(true);

        when(service.updateSettings(Mockito.any(NotificationSettings.class))).thenReturn(settings);

        String jsonContent = "{ " +
                "\"mobilePushNotifications\": true, " +
                "\"emailNotificationActivityInWorkspace\": true, " +
                "\"emailNotificationAlwaysSendEmailNotifications\": false, " +
                "\"emailNotificationEmailDigest\": true, " +
                "\"emailNotificationAnnouncementAndUpdateEmails\": false, " +
                "\"slackNotificationsActivityOnYourWorkspace\": true, " +
                "\"slackNotificationsAlwaysSendEmailNotifications\": false, " +
                "\"slackNotificationsAnnouncementAndUpdateEmails\": true " +
                "}";

        mockMvc.perform(patch("/api/v1/notification-settings")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"status\":\"success\",\"message\":\"Notification preferences updated successfully\",\"status_code\":200,\"data\":{}}"));
    }

}
