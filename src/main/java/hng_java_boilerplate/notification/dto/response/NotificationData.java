package hng_java_boilerplate.notification.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record NotificationData(
        int total_notification_count,
        int total_unread_notification_count,
        List<NotificationDto> notifications) {}
