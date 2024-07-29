package hng_java_boilerplate.user.dto.request;

import hng_java_boilerplate.user.exception.InvalidRequestException;
import hng_java_boilerplate.util.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @Email(message = "Invalid email or password")
    @NotBlank(message = "Invalid email or password")
    private String email;

    @ValidPassword(message = "Invalid email or password")
    @NotBlank(message = "Invalid email or password")
    private String password;
}
