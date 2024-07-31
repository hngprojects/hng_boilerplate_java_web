package hng_java_boilerplate.organisation.dto;

import lombok.Builder;

@Builder
public record UpdateOrganisationResponseDto(
        String message,
        UpdateOrgData org
) {
}
