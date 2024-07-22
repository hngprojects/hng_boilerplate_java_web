package hng_java_boilerplate.notificationSettings.repository;

import hng_java_boilerplate.notificationSettings.entity.NotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Long>{
       Optional <NotificationSettings> findByUserId(String userId);
}
