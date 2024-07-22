package hng_java_boilerplate.notificationSettings.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSettings {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name="user_id", nullable = false)
    @Email
    private String userId;
    @Column(name="email_notification", nullable = false)
    private  Boolean emailNotification;
    @Column(name="pushNotification", nullable = false)
    private Boolean pushNotification;
    @Column(name="sms_notification", nullable = false)
    private Boolean smsNotification;
}
