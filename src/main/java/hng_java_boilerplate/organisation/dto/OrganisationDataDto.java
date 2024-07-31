package hng_java_boilerplate.organisation.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OrganisationDataDto(
        List<OrganisationsListDto> organisations
) {
}
