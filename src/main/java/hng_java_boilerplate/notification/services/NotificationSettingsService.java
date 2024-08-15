package hng_java_boilerplate.notification.services;

import hng_java_boilerplate.notification.dto.request.NotificationSettingsRequestDTO;
import hng_java_boilerplate.notification.dto.response.NotificationSettingsResponseDTO;
import hng_java_boilerplate.notification.models.NotificationSettings;
import hng_java_boilerplate.notification.repositories.NotificationSettingsRepository;
import hng_java_boilerplate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSettingsService {
    private final NotificationSettingsRepository repository;
    private final UserService userService;

    public NotificationSettingsResponseDTO getSettings() {
        String userId = userService.getLoggedInUser().getId();
        if (userId == null) {
            throw new IllegalStateException("User not logged in");
        }
        NotificationSettings settings = repository.findByUserId(userId);
        if (settings == null) {
            throw new IllegalStateException("Notification settings not found for user: " + userId);
        }
        return mapToResponseDTO(settings);
    }

    public NotificationSettingsResponseDTO updateSettings(NotificationSettingsRequestDTO settingsDTO) {
        String userId = userService.getLoggedInUser().getId();
        if (userId == null) {
            throw new IllegalStateException("User not logged in");
        }
        NotificationSettings existingSettings = repository.findByUserId(userId);
        if (existingSettings != null) {
            updateSettingsFromDTO(existingSettings, settingsDTO);
            NotificationSettings updatedSettings = repository.save(existingSettings);
            return mapToResponseDTO(updatedSettings);
        } else {
            throw new IllegalStateException("Notification settings not found for user: " + userId);
        }
    }

    private NotificationSettingsResponseDTO mapToResponseDTO(NotificationSettings settings) {
        NotificationSettingsResponseDTO dto = new NotificationSettingsResponseDTO();
        dto.setMobilePushNotifications(settings.getMobilePushNotifications());
        dto.setEmailNotificationActivityInWorkspace(settings.getEmailNotificationActivityInWorkspace());
        dto.setEmailNotificationAlwaysSendEmailNotifications(settings.getEmailNotificationAlwaysSendEmailNotifications());
        dto.setEmailNotificationEmailDigest(settings.getEmailNotificationEmailDigest());
        dto.setEmailNotificationAnnouncementAndUpdateEmails(settings.getEmailNotificationAnnouncementAndUpdateEmails());
        dto.setSlackNotificationsActivityOnYourWorkspace(settings.getSlackNotificationsActivityOnYourWorkspace());
        dto.setSlackNotificationsAlwaysSendEmailNotifications(settings.getSlackNotificationsAlwaysSendEmailNotifications());
        dto.setSlackNotificationsAnnouncementAndUpdateEmails(settings.getSlackNotificationsAnnouncementAndUpdateEmails());
        return dto;
    }

    private void updateSettingsFromDTO(NotificationSettings existingSettings, NotificationSettingsRequestDTO settingsDTO) {
        existingSettings.setMobilePushNotifications(settingsDTO.isMobilePushNotifications());
        existingSettings.setEmailNotificationActivityInWorkspace(settingsDTO.isEmailNotificationActivityInWorkspace());
        existingSettings.setEmailNotificationAlwaysSendEmailNotifications(settingsDTO.isEmailNotificationAlwaysSendEmailNotifications());
        existingSettings.setEmailNotificationEmailDigest(settingsDTO.isEmailNotificationEmailDigest());
        existingSettings.setEmailNotificationAnnouncementAndUpdateEmails(settingsDTO.isEmailNotificationAnnouncementAndUpdateEmails());
        existingSettings.setSlackNotificationsActivityOnYourWorkspace(settingsDTO.isSlackNotificationsActivityOnYourWorkspace());
        existingSettings.setSlackNotificationsAlwaysSendEmailNotifications(settingsDTO.isSlackNotificationsAlwaysSendEmailNotifications());
        existingSettings.setSlackNotificationsAnnouncementAndUpdateEmails(settingsDTO.isSlackNotificationsAnnouncementAndUpdateEmails());
    }

}