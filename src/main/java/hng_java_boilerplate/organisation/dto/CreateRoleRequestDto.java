package hng_java_boilerplate.organisation.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record CreateRoleRequestDto (
        @NotBlank(message = "Role name is required")
        String name,

        @NotBlank(message = "At least one permission id is required")
        Set<String> permission_ids
){
}
