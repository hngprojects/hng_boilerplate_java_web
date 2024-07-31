package hng_java_boilerplate.organisation.dto;

import lombok.Builder;

@Builder
public record OrgUsersPaginatedResponseDto(
        String id,
        String email,
        String phone_number,
        String name
) {
}
