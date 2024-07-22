package hng_java_boilerplate.notificationSettings.service;

import hng_java_boilerplate.notificationSettings.entity.NotificationSettings;
import hng_java_boilerplate.notificationSettings.repository.NotificationSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationSettingsService {
    @Autowired
    private NotificationSettingsRepository settingsRepository;

    public NotificationSettings save(NotificationSettings settings){
        return settingsRepository.save(settings);
    }
    public Optional<NotificationSettings> findByUserId(String userId){
        return settingsRepository.findByUserId(userId);
    }
}
