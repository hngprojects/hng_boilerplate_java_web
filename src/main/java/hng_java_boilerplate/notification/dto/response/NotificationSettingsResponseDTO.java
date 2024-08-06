package hng_java_boilerplate.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NotificationSettingsResponseDTO {
    @JsonProperty("mobile_push_notifications")
    private boolean mobilePushNotifications;

    @JsonProperty("email_notification_activity_in_workspace")
    private boolean emailNotificationActivityInWorkspace;

    @JsonProperty("email_notification_always_send_email_notifications")
    private boolean emailNotificationAlwaysSendEmailNotifications;

    @JsonProperty("email_notification_email_digest")
    private boolean emailNotificationEmailDigest;

    @JsonProperty("email_notification_announcement_and_update_emails")
    private boolean emailNotificationAnnouncementAndUpdateEmails;

    @JsonProperty("slack_notifications_activity_on_your_workspace")
    private boolean slackNotificationsActivityOnYourWorkspace;

    @JsonProperty("slack_notifications_always_send_email_notifications")
    private boolean slackNotificationsAlwaysSendEmailNotifications;

    @JsonProperty("slack_notifications_announcement_and_update_emails")
    private boolean slackNotificationsAnnouncementAndUpdateEmails;
}
