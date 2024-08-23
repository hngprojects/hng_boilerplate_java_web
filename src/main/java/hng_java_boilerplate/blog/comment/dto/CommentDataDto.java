package hng_java_boilerplate.blog.comment.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class CommentDataDto {
    private String user_id;
    private String userName;

    private String comment;
    private String createdAt;
}
