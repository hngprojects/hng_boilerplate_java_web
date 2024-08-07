package hng_java_boilerplate.comment.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import hng_java_boilerplate.user.entity.User;
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
