package hng_java_boilerplate.notification.controllers;

import hng_java_boilerplate.notification.dto.request.NotificationSettingsRequestDTO;
import hng_java_boilerplate.notification.dto.response.ApiResponseDTO;
import hng_java_boilerplate.notification.dto.response.NotificationSettingsResponseDTO;
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
    public ResponseEntity<ApiResponseDTO<NotificationSettingsResponseDTO>> getSettings() {
        NotificationSettingsResponseDTO settings = service.getSettings();
        ApiResponseDTO<NotificationSettingsResponseDTO> response = new ApiResponseDTO<>();
        response.setStatus("success");
        response.setMessage("Notification preferences retrieved successfully");
        response.setStatusCode(200);
        response.setData(settings);
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<ApiResponseDTO<NotificationSettingsResponseDTO>> updateSettings(@RequestBody NotificationSettingsRequestDTO settingsDTO) {
        NotificationSettingsResponseDTO updatedSettings = service.updateSettings(settingsDTO);
        ApiResponseDTO<NotificationSettingsResponseDTO> response = new ApiResponseDTO<>();
        response.setStatus("success");
        response.setMessage("Notification preferences updated successfully");
        response.setStatusCode(200);
        response.setData(updatedSettings);
        return ResponseEntity.ok(response);
    }
}

