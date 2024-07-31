package hng_java_boilerplate.organisation.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record RoleDataDto(
        String id,
        String name,
        Set<PermissionsListDto> permissions,
        LocalDateTime created_at

) {
}
