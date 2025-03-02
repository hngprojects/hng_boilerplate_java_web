package hng_java_boilerplate.organisation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record AddUserRequestDTO(
        @NotEmpty(message = "User ID cannot be empty")
        @Size(min = 1, message = "Provide a valid user ID")
        List<String> user_ids
) {
}