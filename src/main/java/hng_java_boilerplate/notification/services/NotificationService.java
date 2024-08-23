package hng_java_boilerplate.notification.services;

import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.notification.dto.request.MarkRead;
import hng_java_boilerplate.notification.dto.response.NotificationData;
import hng_java_boilerplate.notification.dto.response.NotificationDto;
import hng_java_boilerplate.notification.dto.response.NotificationDtoRes;
import hng_java_boilerplate.notification.dto.response.NotificationResponse;
import hng_java_boilerplate.notification.models.Notification;
import hng_java_boilerplate.notification.models.NotificationSettings;
import hng_java_boilerplate.notification.repositories.NotificationRepository;
import hng_java_boilerplate.user.entity.User;
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

    public NotificationResponse getAllNotifications() {
        List<Notification> notifications =  repository.findAll();

        List<NotificationDto> dto = notifications
                .stream().map((notification -> NotificationDto
                .builder()
                .notification_id(notification.getNotificationId())
                .is_read(notification.getIsRead())
                .message(notification.getMessage())
                .created_at(notification.getCreatedAt().toString())
                .build())).toList();

        NotificationData data = NotificationData.builder()
                .total_notification_count(notifications.size())
                .total_unread_notification_count(getTotalUnreadNotificationCount())
                .notifications(dto)
                .build();

        return NotificationResponse
                .builder()
                .status("success")
                .message("Notifications retrieved successfully")
                .status_code(200)
                .data(data)
                .build();
    }

    public int getTotalUnreadNotificationCount() {
        return repository.countByIsRead(false);
    }

    public NotificationResponse getUnreadNotifications(boolean isRead) {
        User user = userService.getLoggedInUser();
        List<Notification> notifications =  repository.findByUserIdAndIsRead(user.getId(), isRead);

        List<NotificationDto> dto = notifications
                .stream().map((notification -> NotificationDto
                .builder()
                .notification_id(notification.getNotificationId())
                .is_read(notification.getIsRead())
                .message(notification.getMessage())
                .created_at(notification.getCreatedAt().toString())
                .build())).toList();

        NotificationData data = NotificationData.builder()
                .total_notification_count(notifications.size())
                .total_unread_notification_count(getTotalUnreadNotificationCount())
                .notifications(dto)
                .build();

        return NotificationResponse
                .builder()
                .status("success")
                .message("Unread notifications retrieved successfully")
                .status_code(200)
                .data(data)
                .build();
    }

    public NotificationResponse createNotification(String message) {
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

        repository.saveAndFlush(notification);

        NotificationDto dto = NotificationDto.builder()
                        .notification_id(notification.getNotificationId())
                        .is_read(notification.getIsRead())
                        .message(notification.getMessage())
                        .created_at(notification.getCreatedAt().toString())
                        .build();

        NotificationData data = NotificationData.builder()
                .notifications(List.of(dto))
                .build();

        return NotificationResponse
                .builder()
                .status("success")
                .message("Notification created successfully")
                .status_code(201)
                .data(data)
                .build();
    }

    public NotificationDtoRes markAsRead(UUID notificationId, MarkRead read) {
        Notification notification = repository.findById(notificationId)
                .orElseThrow(()-> new NotFoundException("Notification not found"));

        notification.setIsRead(read.is_read());
        repository.saveAndFlush(notification);

        NotificationDto dto = NotificationDto.builder()
                .notification_id(notification.getNotificationId())
                .is_read(notification.getIsRead())
                .message(notification.getMessage())
                .created_at(notification.getCreatedAt().toString())
                .build();

        return NotificationDtoRes
                .builder()
                .status("success")
                .message("Notifications cleared successfully")
                .status_code(200)
                .data(dto)
                .build();
    }

    public NotificationResponse markAllAsRead() {
        List<Notification> notifications = repository.findByIsRead(false);
        notifications.forEach(notification -> {
            notification.setIsRead(true);
            repository.save(notification);
        });
        List<NotificationDto> dto = notifications
                .stream().map((notification -> NotificationDto
                        .builder()
                        .notification_id(notification.getNotificationId())
                        .is_read(notification.getIsRead())
                        .message(notification.getMessage())
                        .created_at(notification.getCreatedAt().toString())
                        .build())).toList();

        NotificationData data = NotificationData.builder()
                .total_notification_count(notifications.size())
                .total_unread_notification_count(getTotalUnreadNotificationCount())
                .notifications(dto)
                .build();

        return NotificationResponse
                .builder()
                .status("success")
                .message("Notifications cleared successfully")
                .status_code(200)
                .data(data)
                .build();
    }
}
