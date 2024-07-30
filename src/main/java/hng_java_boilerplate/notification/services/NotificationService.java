package hng_java_boilerplate.notification.services;

import hng_java_boilerplate.notification.models.Notification;
import hng_java_boilerplate.notification.repositories.NotificationRepository;
import hng_java_boilerplate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Notification> getUnreadNotifications(UUID userId) {
        return repository.findByUserIdAndIsRead(userId, false);
    }

    public Notification createNotification(String message) {
        // Get the current logged-in user
        String userId = userService.getLoggedInUser().getId();

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUserId(userId);  // Set the userId in the notification
        return repository.save(notification);
    }

    public Notification markAsRead(UUID notificationId) {
        Notification notification = repository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setIsRead(true);
            return repository.save(notification);
        }
        return null;
    }

    public List<Notification> markAllAsRead() {
        List<Notification> notifications = repository.findByIsRead(false);
        notifications.forEach(notification -> {
            notification.setIsRead(true);
            repository.save(notification);
        });
        return notifications; // Return the list of notifications that were marked as read
    }
}
