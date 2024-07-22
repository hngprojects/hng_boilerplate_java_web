package hng_java_boilerplate.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private Integer statusCode;

    public ApiResponse(String status, String message, T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public ApiResponse(String message, T data){
        this.message = message;
        this.data = data;
    }
    public ApiResponse(String status, String message, Integer statusCode){
        this.status = status;
        this.message = message;
        this.statusCode = statusCode;
    }
    public ApiResponse(String status, String message){
        this.status = status;
        this.message = message;

    }
}

