package hng_java_boilerplate.messaging.notification.repositories;

import hng_java_boilerplate.messaging.notification.models.NotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Long> {
    NotificationSettings findByUserId(String userId);
    @Query("SELECT ns FROM NotificationSettings ns") // Assuming there's only one record
    NotificationSettings findFirst();
}