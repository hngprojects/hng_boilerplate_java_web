package hng_java_boilerplate.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditRequest {
    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "content cannot be empty")
    private String content;

    @NotEmpty(message = "tags list cannot be empty")
    private List<@NotBlank(message = "tag cannot be blank") String> tags;
}
