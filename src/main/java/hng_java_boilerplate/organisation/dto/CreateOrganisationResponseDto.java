package hng_java_boilerplate.organisation.dto;

import lombok.Builder;

@Builder
public record CreateOrganisationResponseDto (
        String status,
        String message,
        DataDto data,
        Integer status_code
) {
}
