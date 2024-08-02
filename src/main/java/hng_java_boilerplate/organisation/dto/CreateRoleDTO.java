package hng_java_boilerplate.organisation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class CreateRoleDTO {

    @NotBlank
    private String name;

    private String description;

    public CreateRoleDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
