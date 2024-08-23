package hng_java_boilerplate.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.config.WebSecurityConfig;
import hng_java_boilerplate.notification.controllers.NotificationSettingsController;
import hng_java_boilerplate.notification.dto.request.NotificationSettingsRequestDTO;
import hng_java_boilerplate.notification.dto.response.NotificationSettingsResponseDTO;
import hng_java_boilerplate.notification.services.NotificationSettingsService;
import hng_java_boilerplate.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@Import({JwtUtils.class, WebSecurityConfig.class})
@WebMvcTest(NotificationSettingsController.class)
public class NotificationSettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationSettingsService notificationSettingsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @WithMockUser(username = "testuser", roles = {"USER"})
    @Test
    public void testGetSettings() throws Exception {
        NotificationSettingsResponseDTO settings = new NotificationSettingsResponseDTO();
        settings.setMobilePushNotifications(true);
        settings.setEmailNotificationActivityInWorkspace(true);
        settings.setEmailNotificationAlwaysSendEmailNotifications(true);
        settings.setEmailNotificationEmailDigest(true);
        settings.setEmailNotificationAnnouncementAndUpdateEmails(true);
        settings.setSlackNotificationsActivityOnYourWorkspace(true);
        settings.setSlackNotificationsAlwaysSendEmailNotifications(true);
        settings.setSlackNotificationsAnnouncementAndUpdateEmails(true);

        when(notificationSettingsService.getSettings()).thenReturn(settings);

        mockMvc.perform(get("/api/v1/notification-settings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message", is("Notification preferences retrieved successfully")))
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.data.mobile_push_notifications", is(true)))
                .andExpect(jsonPath("$.data.email_notification_activity_in_workspace", is(true)))
                .andExpect(jsonPath("$.data.email_notification_always_send_email_notifications", is(true)))
                .andExpect(jsonPath("$.data.email_notification_email_digest", is(true)))
                .andExpect(jsonPath("$.data.email_notification_announcement_and_update_emails", is(true)))
                .andExpect(jsonPath("$.data.slack_notifications_activity_on_your_workspace", is(true)))
                .andExpect(jsonPath("$.data.slack_notifications_always_send_email_notifications", is(true)))
                .andExpect(jsonPath("$.data.slack_notifications_announcement_and_update_emails", is(true)));
    }

    @WithMockUser(username = "testuser", roles = {"USER"})
    @Test
    public void testUpdateSettings() throws Exception {
        NotificationSettingsRequestDTO requestDTO = new NotificationSettingsRequestDTO();
        requestDTO.setMobilePushNotifications(false);
        requestDTO.setEmailNotificationActivityInWorkspace(false);
        requestDTO.setEmailNotificationAlwaysSendEmailNotifications(false);
        requestDTO.setEmailNotificationEmailDigest(false);
        requestDTO.setEmailNotificationAnnouncementAndUpdateEmails(false);
        requestDTO.setSlackNotificationsActivityOnYourWorkspace(false);
        requestDTO.setSlackNotificationsAlwaysSendEmailNotifications(false);
        requestDTO.setSlackNotificationsAnnouncementAndUpdateEmails(false);

        NotificationSettingsResponseDTO responseDTO = new NotificationSettingsResponseDTO();
        responseDTO.setMobilePushNotifications(false);
        responseDTO.setEmailNotificationActivityInWorkspace(false);
        responseDTO.setEmailNotificationAlwaysSendEmailNotifications(false);
        responseDTO.setEmailNotificationEmailDigest(false);
        responseDTO.setEmailNotificationAnnouncementAndUpdateEmails(false);
        responseDTO.setSlackNotificationsActivityOnYourWorkspace(false);
        responseDTO.setSlackNotificationsAlwaysSendEmailNotifications(false);
        responseDTO.setSlackNotificationsAnnouncementAndUpdateEmails(false);

        when(notificationSettingsService.updateSettings(any(NotificationSettingsRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(patch("/api/v1/notification-settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message", is("Notification preferences updated successfully")))
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.data.mobile_push_notifications", is(false)))
                .andExpect(jsonPath("$.data.email_notification_activity_in_workspace", is(false)))
                .andExpect(jsonPath("$.data.email_notification_always_send_email_notifications", is(false)))
                .andExpect(jsonPath("$.data.email_notification_email_digest", is(false)))
                .andExpect(jsonPath("$.data.email_notification_announcement_and_update_emails", is(false)))
                .andExpect(jsonPath("$.data.slack_notifications_activity_on_your_workspace", is(false)))
                .andExpect(jsonPath("$.data.slack_notifications_always_send_email_notifications", is(false)))
                .andExpect(jsonPath("$.data.slack_notifications_announcement_and_update_emails", is(false)));
    }
}
