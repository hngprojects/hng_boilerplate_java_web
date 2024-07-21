package hng_java_boilerplate.exception;

import java.util.List;

public class ErrorResponse {

    private String error;
    private String message;
    private List<?> errors;

    public ErrorResponse(List<ErrorResponse> errors) {
        this.errors= errors;
    }

    public List<?> getErrors() {
        return errors;
    }

    public void setErrors(List<?> errors) {
        this.errors = errors;
    }

//    public ErrorResponse() {
//    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }
}
