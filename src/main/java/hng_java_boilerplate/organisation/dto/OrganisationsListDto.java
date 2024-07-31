package hng_java_boilerplate.organisation.dto;

import lombok.Builder;

@Builder
public record OrganisationsListDto(
        String orgId,
        String name,
        String description
) {
}
