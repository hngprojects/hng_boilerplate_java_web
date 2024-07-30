package hng_java_boilerplate.job.models;

import lombok.Data;

@Data
public class ResponseWrapper<T> {
    private String status;
    private String message;
    private T data;

   
    public ResponseWrapper(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    
    public ResponseWrapper(String status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }

    
}
