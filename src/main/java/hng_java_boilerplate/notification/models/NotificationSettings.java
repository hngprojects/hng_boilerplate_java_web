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

    private Boolean mobilePushNotifications = false;
    private Boolean emailNotificationActivityInWorkspace = false;
    private Boolean emailNotificationAlwaysSendEmailNotifications = false;
    private Boolean emailNotificationEmailDigest = false;
    private Boolean emailNotificationAnnouncementAndUpdateEmails = false;
    private Boolean slackNotificationsActivityOnYourWorkspace = false;
    private Boolean slackNotificationsAlwaysSendEmailNotifications = false;
    private Boolean slackNotificationsAnnouncementAndUpdateEmails = false;
}