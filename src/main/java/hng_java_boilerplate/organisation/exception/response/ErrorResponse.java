package hng_java_boilerplate.organisation.exception.response;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private List<String> error;
    private int status;
}
