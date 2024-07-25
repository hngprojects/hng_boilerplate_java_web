package hng_java_boilerplate.notificationSettings.service;

import hng_java_boilerplate.notificationSettings.entity.NotificationSettings;
import hng_java_boilerplate.notificationSettings.repository.NotificationSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationSettingsService {

    private  final NotificationSettingsRepository settingsRepository;

    public NotificationSettings save(NotificationSettings settings){
        return settingsRepository.save(settings);
    }
    public Optional<NotificationSettings> findByUserId(String userId){
        return settingsRepository.findByUserId(userId);
    }
}
