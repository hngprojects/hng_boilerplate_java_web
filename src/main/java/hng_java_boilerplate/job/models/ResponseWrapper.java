package hng_java_boilerplate.job.models;

import lombok.Data;

@Data
public class ResponseWrapper<T> {
    private String status;
    private String message;
    private T data;

    // Constructor for success response
    public ResponseWrapper(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Constructor for error response
    public ResponseWrapper(String status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }

    // Getters and setters
}
