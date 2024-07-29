package hng_java_boilerplate.organisation.dto;

import lombok.Builder;

@Builder
public record PermissionsListDto(
        String id,
        String name
) {
}
