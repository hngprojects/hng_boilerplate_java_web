package hng_java_boilerplate.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailSenderDto {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
}
