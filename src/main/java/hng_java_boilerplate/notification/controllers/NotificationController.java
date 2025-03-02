package hng_java_boilerplate.notification.controllers;

import hng_java_boilerplate.categories.dto.CategoryDto;
import hng_java_boilerplate.exception.ErrorResponseDto;
import hng_java_boilerplate.exception.ValidationError;
import hng_java_boilerplate.notification.dto.request.MarkRead;
import hng_java_boilerplate.notification.dto.request.NotificationRequest;
import hng_java_boilerplate.notification.dto.response.NotificationDtoRes;
import hng_java_boilerplate.notification.dto.response.NotificationResponse;
import hng_java_boilerplate.notification.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "Controller for Notification")
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "get all notifications success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @Operation(summary = "Get all Notifications")
    public ResponseEntity<NotificationResponse> getAllNotifications() {
        return ResponseEntity.ok(service.getAllNotifications());
    }


    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "create notification success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NotificationResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "User not admin",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "422", description = "Invalid or missing required request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationError.class))
            ),
    })
    @Operation(summary = "User with admin privilege create notification")
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody @Valid NotificationRequest request) {
        return ResponseEntity.status(201).body(service.createNotification(request.getMessage()));
    }


    @PatchMapping("/{notificationId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "admin user marks a notification as read using id of the notification")
    public ResponseEntity<NotificationDtoRes> markAsRead(@PathVariable UUID notificationId, @RequestBody @Valid MarkRead isRead) {
        return ResponseEntity.ok(service.markAsRead(notificationId, isRead));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "admin user mark all notifications as read")
    public ResponseEntity<NotificationResponse> markAllAsRead() {
        return ResponseEntity.ok(service.markAllAsRead());
    }
}