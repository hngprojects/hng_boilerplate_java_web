package hng_java_boilerplate.notification.dto.response;

import lombok.Data;

@Data
public class ApiResponseDTO <T> {
    private String status;
    private String message;
    private int statusCode;
    private T data;
}
