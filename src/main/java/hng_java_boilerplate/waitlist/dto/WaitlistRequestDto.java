package hng_java_boilerplate.waitlist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WaitlistRequestDto {
    @JsonProperty("full_name")
    @NotBlank(message = "full_name is required")
    private String fullName;

    @JsonProperty("email")
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
}
