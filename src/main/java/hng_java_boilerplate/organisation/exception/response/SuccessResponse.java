package hng_java_boilerplate.organisation.exception.response;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SuccessResponse {
    private String message;
    private int status;
}
