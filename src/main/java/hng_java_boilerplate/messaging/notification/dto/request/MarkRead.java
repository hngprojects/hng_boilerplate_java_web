package hng_java_boilerplate.messaging.notification.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MarkRead {
    @NotNull(message = "is_read is required and should be a boolean")
    private boolean is_read;
}
