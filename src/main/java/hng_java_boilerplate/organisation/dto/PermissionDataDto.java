package hng_java_boilerplate.organisation.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PermissionDataDto(
        String id,
        String name,
        String description,
        LocalDateTime created_at
) {
}
