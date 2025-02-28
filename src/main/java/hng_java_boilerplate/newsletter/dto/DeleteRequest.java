package hng_java_boilerplate.newsletter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteRequest {
    @NotBlank(message = "user_id is required")
    private String user_id;
}
