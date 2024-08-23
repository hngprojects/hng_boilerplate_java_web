package hng_java_boilerplate.authentication.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import hng_java_boilerplate.util.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {
    @JsonProperty("first_name")
    @NotBlank(message = "First name is required")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;
}