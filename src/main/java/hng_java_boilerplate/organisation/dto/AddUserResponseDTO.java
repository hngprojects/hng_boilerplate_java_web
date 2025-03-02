package hng_java_boilerplate.organisation.dto;

import hng_java_boilerplate.organisation.interfaces.AddUserResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record AddUserResponseDTO(
        String status,
        String message,
        String organization_id,
        List<String> users_added_to_organisation,
        Integer status_code
) implements AddUserResponse {
}
