package hng_java_boilerplate.notification.controllers;

import hng_java_boilerplate.notification.dto.request.MarkRead;
import hng_java_boilerplate.notification.dto.request.NotificationRequest;
import hng_java_boilerplate.notification.dto.response.NotificationDtoRes;
import hng_java_boilerplate.notification.dto.response.NotificationResponse;
import hng_java_boilerplate.notification.models.Notification;
import hng_java_boilerplate.notification.services.NotificationService;
import jakarta.validation.Valid;
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
    public ResponseEntity<NotificationResponse> getAllNotifications() {
        return ResponseEntity.ok(service.getAllNotifications());
    }


    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody @Valid NotificationRequest request) {
        return ResponseEntity.status(201).body(service.createNotification(request.getMessage()));
    }


    @PatchMapping("/{notificationId}")
    public ResponseEntity<NotificationDtoRes> markAsRead(@PathVariable UUID notificationId, @RequestBody @Valid MarkRead isRead) {
        return ResponseEntity.ok(service.markAsRead(notificationId, isRead));
    }

    @DeleteMapping
    public ResponseEntity<NotificationResponse> markAllAsRead() {
        return ResponseEntity.ok(service.markAllAsRead());
    }
}