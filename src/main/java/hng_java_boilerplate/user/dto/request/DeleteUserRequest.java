package hng_java_boilerplate.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteUserRequest {

    @NotBlank(message = "Email is required and cannot be blank.")
    private String email;

}
