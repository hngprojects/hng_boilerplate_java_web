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
            existingSettings.setMobile_push_notifications(settings.getMobile_push_notifications());
            existingSettings.setEmail_notification_activity_in_workspace(settings.getEmail_notification_activity_in_workspace());
            existingSettings.setEmail_notification_always_send_email_notifications(settings.getEmail_notification_always_send_email_notifications());
            existingSettings.setEmail_notification_email_digest(settings.getEmail_notification_email_digest());
            existingSettings.setEmail_notification_announcement_and_update_emails(settings.getEmail_notification_announcement_and_update_emails());
            existingSettings.setSlack_notifications_activity_on_your_workspace(settings.getSlack_notifications_activity_on_your_workspace());
            existingSettings.setSlack_notifications_always_send_email_notifications(settings.getSlack_notifications_always_send_email_notifications());
            existingSettings.setSlack_notifications_announcement_and_update_emails(settings.getSlack_notifications_announcement_and_update_emails());
            return repository.save(existingSettings);
        } else {
            // Handle case where settings do not exist, maybe throw an exception or create new settings
            throw new IllegalStateException("Notification settings not found for user: " + userId);
        }
    }
}