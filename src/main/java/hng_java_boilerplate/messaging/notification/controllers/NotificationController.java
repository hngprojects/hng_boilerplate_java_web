package hng_java_boilerplate.messaging.notification.controllers;

import hng_java_boilerplate.messaging.notification.services.NotificationService;
import hng_java_boilerplate.messaging.notification.dto.request.MarkRead;
import hng_java_boilerplate.messaging.notification.dto.request.NotificationRequest;
import hng_java_boilerplate.messaging.notification.dto.response.NotificationDtoRes;
import hng_java_boilerplate.messaging.notification.dto.response.NotificationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody @Valid NotificationRequest request) {
        return ResponseEntity.status(201).body(service.createNotification(request.getMessage()));
    }


    @PatchMapping("/{notificationId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<NotificationDtoRes> markAsRead(@PathVariable UUID notificationId, @RequestBody @Valid MarkRead isRead) {
        return ResponseEntity.ok(service.markAsRead(notificationId, isRead));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<NotificationResponse> markAllAsRead() {
        return ResponseEntity.ok(service.markAllAsRead());
    }
}