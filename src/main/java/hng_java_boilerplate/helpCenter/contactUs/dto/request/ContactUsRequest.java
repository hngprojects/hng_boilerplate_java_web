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

    @NotBlank(message = "phone_no is required")
    private String phone_no;

    @NotBlank(message = "message is required")
    private String message;
}
