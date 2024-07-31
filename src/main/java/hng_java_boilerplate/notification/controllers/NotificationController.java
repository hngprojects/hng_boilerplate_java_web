package hng_java_boilerplate.notification.controllers;

import hng_java_boilerplate.notification.models.Notification;
import hng_java_boilerplate.notification.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public ResponseEntity<?> getAllNotifications() {
        List<Notification> notifications = service.getAllNotifications();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Notifications retrieved successfully");
        response.put("status_code", 200);

        Map<String, Object> data = new HashMap<>();
        data.put("total_notification_count", notifications.size());
        data.put("total_unread_notification_count", service.getTotalUnreadNotificationCount());
        data.put("notifications", notifications);

        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody Map<String, String> request) {
        String message = request.get("message");

        Notification notification = service.createNotification(message);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Notification created successfully");
        response.put("status_code", 201);

        Map<String, Object> data = new HashMap<>();
        data.put("notifications", Collections.singletonList(notification));
        response.put("data", data);

        return ResponseEntity.status(201).body(response);
    }


    @PatchMapping("/{notificationId}")
    public ResponseEntity<?> markAsRead(@PathVariable UUID notificationId) {
        Notification notification = service.markAsRead(notificationId);
        if (notification == null) {
            return ResponseEntity.status(404).body("Notification not found");
        }
        return ResponseEntity.ok(notification);
    }

    @DeleteMapping
    public ResponseEntity<?> markAllAsRead() {
        List<Notification> notifications = service.markAllAsRead();

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Notifications cleared successfully");
        response.put("status_code", 200);

        Map<String, Object> data = new HashMap<>();
        data.put("notifications", new ArrayList<>()); // Empty list as per the response format
        response.put("data", data);

        return ResponseEntity.ok(response);
    }
}