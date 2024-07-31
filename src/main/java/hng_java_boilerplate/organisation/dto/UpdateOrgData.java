package hng_java_boilerplate.organisation.dto;

import lombok.Builder;

@Builder
public record UpdateOrgData(
        String name,
        String description
) {
}
