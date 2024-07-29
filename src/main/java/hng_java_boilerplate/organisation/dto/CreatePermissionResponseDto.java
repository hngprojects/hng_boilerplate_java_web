package hng_java_boilerplate.organisation.dto;

import lombok.Builder;

@Builder
public record CreatePermissionResponseDto(
        String message,
        Integer status_code,
        PermissionDataDto data
) {
}
