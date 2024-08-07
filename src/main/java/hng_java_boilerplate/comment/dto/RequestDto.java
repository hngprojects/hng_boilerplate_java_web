package hng_java_boilerplate.comment.dto;

import hng_java_boilerplate.blogPosts.entity.BlogPost;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDto {

    @NotBlank(message = "input comment")
    @Size(max = 1000, message = "comment should not exceed 1000 characters")
    private String comment;

}
