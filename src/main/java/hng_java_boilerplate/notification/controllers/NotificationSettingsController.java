package hng_java_boilerplate.notification.controllers;

import hng_java_boilerplate.notification.models.NotificationSettings;
import hng_java_boilerplate.notification.services.NotificationSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notification-settings")
@RequiredArgsConstructor
public class NotificationSettingsController {

    private final NotificationSettingsService service;

    @GetMapping
    public ResponseEntity<?> getSettings() {
        NotificationSettings settings = service.getSettings();
        if (settings == null) {
            return ResponseEntity.status(404).body("Settings not found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Notification preferences retrieved successfully");
        response.put("status_code", 200);
        response.put("data", settings);

        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<?> updateSettings(@RequestBody NotificationSettings settings) {
        NotificationSettings updatedSettings = service.updateSettings(settings);
        if (updatedSettings == null) {
            return ResponseEntity.status(404).body("Settings not found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Notification preferences updated successfully");
        response.put("status_code", 200);
        response.put("data", updatedSettings);

        return ResponseEntity.ok(response);
    }
}

