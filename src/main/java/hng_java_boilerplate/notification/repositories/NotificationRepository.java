package hng_java_boilerplate.notification.repositories;

import hng_java_boilerplate.notification.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserIdAndIsRead(String userId, Boolean isRead);

    int countByIsRead(boolean b);
    List<Notification> findByIsRead(Boolean isRead);
}