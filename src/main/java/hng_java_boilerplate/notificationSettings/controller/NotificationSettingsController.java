package hng_java_boilerplate.notificationSettings.controller;

import hng_java_boilerplate.notificationSettings.entity.NotificationSettings;
import hng_java_boilerplate.notificationSettings.service.NotificationSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class NotificationSettingsController {
    @Autowired
    private NotificationSettingsService notificationSettingsService;

    @PostMapping("api/v1/settings/notification-settings")
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<?> createOrUpdateNotificationSettings(@RequestBody NotificationSettings settings, BindingResult result){
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        NotificationSettings savedSettings = notificationSettingsService.save(settings);
        return ResponseEntity.ok(savedSettings);
    }

    @GetMapping("api/v1/settings/notification-settings/{user-Id}")
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<?> getNotificationSettings(@PathVariable String userId) {
        return notificationSettingsService.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }
}