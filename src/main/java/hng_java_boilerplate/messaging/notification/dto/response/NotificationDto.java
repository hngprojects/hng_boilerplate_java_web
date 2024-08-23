package hng_java_boilerplate.messaging.notification.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NotificationDto {
    private UUID notification_id;
    private boolean is_read;
    private String message;
    private String created_at;
}
