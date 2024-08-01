package hng_java_boilerplate.notification.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "notification_settings")
public class NotificationSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private Boolean mobile_push_notifications = false;
    private Boolean email_notification_activity_in_workspace = false;
    private Boolean email_notification_always_send_email_notifications = false;
    private Boolean email_notification_email_digest = false;
    private Boolean email_notification_announcement_and_update_emails = false;
    private Boolean slack_notifications_activity_on_your_workspace = false;
    private Boolean slack_notifications_always_send_email_notifications = false;
    private Boolean slack_notifications_announcement_and_update_emails = false;
}