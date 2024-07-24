package hng_java_boilerplate.sale.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private Boolean status;
    private int status_code;
    private String error;
    private String message;
    private ErrorDetails details;

    public ErrorResponse(Boolean status, int status_code, String error, String message){
        this.status = status;
        this.status_code = status_code;
        this.error = error;
        this.message = message;
    }
}
