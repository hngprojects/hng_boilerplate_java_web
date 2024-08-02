package hng_java_boilerplate.notification.dto.response;

import lombok.Builder;

@Builder
public record NotificationResponse(String status, String message, int status_code, NotificationData data) {
}
