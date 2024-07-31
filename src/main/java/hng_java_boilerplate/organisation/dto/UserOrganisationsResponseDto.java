package hng_java_boilerplate.organisation.dto;

import lombok.Builder;

@Builder
public record UserOrganisationsResponseDto(
        String status,
        String message,
        OrganisationDataDto data
) {
}
