package hng_java_boilerplate.helpCenter.contactUs.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactUsRequest {
    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    private String email;

    @NotBlank(message = "phone_number is required")
    private String phone_number;

    @NotBlank(message = "message is required")
    private String message;
}
