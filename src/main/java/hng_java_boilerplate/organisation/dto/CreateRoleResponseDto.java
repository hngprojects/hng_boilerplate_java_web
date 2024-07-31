package hng_java_boilerplate.organisation.dto;

import lombok.Builder;

@Builder
public record CreateRoleResponseDto (
        String message,
        Integer status_code,
        RoleDataDto data
){
}
