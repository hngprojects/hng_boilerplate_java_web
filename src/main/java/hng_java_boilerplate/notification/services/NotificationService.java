package hng_java_boilerplate.notification.services;

import hng_java_boilerplate.helpCenter.topic.exceptions.ResourceNotFoundException;
import hng_java_boilerplate.notification.models.Notification;
import hng_java_boilerplate.notification.models.NotificationSettings;
import hng_java_boilerplate.notification.repositories.NotificationRepository;
import hng_java_boilerplate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;
    private final UserService userService;

    public List<Notification> getAllNotifications() {
        return repository.findAll();
    }

    public int getTotalUnreadNotificationCount() {
        return repository.countByIsRead(false);
    }

    public List<Notification> getUnreadNotifications(String userId) {
        return repository.findByUserIdAndIsRead(userId, false);
    }

    public Notification createNotification(String message) {
        // Get the current logged-in user
        String userId = userService.getLoggedInUser().getId();

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUserId(userId);

        NotificationSettings settings = new NotificationSettings();
        settings.setUserId(userId);
        notification.setNotificationSettings(settings);

        return repository.save(notification);
    }

    public Notification markAsRead(UUID notificationId) {
        Notification notification = repository.findById(notificationId).orElseThrow(()-> new ResourceNotFoundException("Notification not found"));
        notification.setIsRead(true);
        return repository.save(notification);
    }

    public List<Notification> markAllAsRead() {
        List<Notification> notifications = repository.findByIsRead(false);
        notifications.forEach(notification -> {
            notification.setIsRead(true);
            repository.save(notification);
        });
        return notifications;
    }
}
