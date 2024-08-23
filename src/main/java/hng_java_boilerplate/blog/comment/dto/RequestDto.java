package hng_java_boilerplate.blog.comment.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestDto {

    @NotBlank(message = "input comment")
    @Size(max = 1000, message = "comment should not exceed 1000 characters")
    private String comment;

}
