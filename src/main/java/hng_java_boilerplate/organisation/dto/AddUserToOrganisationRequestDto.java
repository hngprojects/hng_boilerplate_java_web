package hng_java_boilerplate.organisation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUserToOrganisationRequestDto {

    @NotBlank(message = "User ID is required")
    private String userId;
}
