package hng_java_boilerplate.organisation.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DataDto(
        String id,
        String name,
        String description,
        String owner_id,
        String slug,
        String email,
        String industry,
        String type,
        String country,
        String address,
        String state,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {
}
