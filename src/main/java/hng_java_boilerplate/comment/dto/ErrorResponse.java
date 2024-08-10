package hng_java_boilerplate.comment.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private  String message;
    private String error;
    private  int status_code;
}
