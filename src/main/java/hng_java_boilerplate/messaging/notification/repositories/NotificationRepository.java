package hng_java_boilerplate.messaging.notification.repositories;

import hng_java_boilerplate.messaging.notification.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserIdAndIsRead(String userId, Boolean isRead);

    int countByIsRead(boolean b);
    List<Notification> findByIsRead(Boolean isRead);
}