package hng_java_boilerplate.notificationSettings.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification_settings")
public class NotificationSettings {
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(name="user_id", nullable = false)
    @Email
    private String userId;
    @Column(name="email_notification", nullable = false)
    private  Boolean emailNotification;
    @Column(name="push_notification", nullable = false)
    private Boolean pushNotification;
    @Column(name="sms_notification", nullable = false)
    private Boolean smsNotification;



}
