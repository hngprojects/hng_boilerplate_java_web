package hng_java_boilerplate.notification.services;

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

    // Fetch userId from the user service
    public NotificationSettings getSettings() {
        String userId = userService.getLoggedInUser().getId();
        if (userId == null) {
            // Handle unauthorized access, maybe throw an exception
            throw new IllegalStateException("User not logged in");
        }
        return repository.findByUserId(userId);
    }

    public NotificationSettings updateSettings(NotificationSettings settings) {
        String userId = userService.getLoggedInUser().getId();
        if (userId == null) {
            // Handle unauthorized access, maybe throw an exception
            throw new IllegalStateException("User not logged in");
        }
        NotificationSettings existingSettings = repository.findByUserId(userId);
        if (existingSettings != null) {
            // Update the settings
            existingSettings.setMobilePushNotifications(settings.getMobilePushNotifications());
            existingSettings.setEmailNotificationActivityInWorkspace(settings.getEmailNotificationActivityInWorkspace());
            existingSettings.setEmailNotificationAlwaysSendEmailNotifications(settings.getEmailNotificationAlwaysSendEmailNotifications());
            existingSettings.setEmailNotificationEmailDigest(settings.getEmailNotificationEmailDigest());
            existingSettings.setEmailNotificationAnnouncementAndUpdateEmails(settings.getEmailNotificationAnnouncementAndUpdateEmails());
            existingSettings.setSlackNotificationsActivityOnYourWorkspace(settings.getSlackNotificationsActivityOnYourWorkspace());
            existingSettings.setSlackNotificationsAlwaysSendEmailNotifications(settings.getSlackNotificationsAlwaysSendEmailNotifications());
            existingSettings.setSlackNotificationsAnnouncementAndUpdateEmails(settings.getSlackNotificationsAnnouncementAndUpdateEmails());
            return repository.save(existingSettings);
        } else {
            // Handle case where settings do not exist, maybe throw an exception or create new settings
            throw new IllegalStateException("Notification settings not found for user: " + userId);
        }
    }
}