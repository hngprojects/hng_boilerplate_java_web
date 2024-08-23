package hng_java_boilerplate.messaging.notification.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Entity
@Setter
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID notificationId;

    private String userId;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_settings_id", referencedColumnName = "id")
    private NotificationSettings notificationSettings;
}
