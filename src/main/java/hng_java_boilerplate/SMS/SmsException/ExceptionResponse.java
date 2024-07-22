package hng_java_boilerplate.SMS.SmsException;

public class ExceptionResponse <G>{
    private String status;
    private int status_code;
    private String message;

    public ExceptionResponse() {
    }

    public ExceptionResponse(String status, int status_code, String message) {
        this.status = status;
        this.status_code = status_code;
        this.message = message;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getStatus_code() {
        return status_code;
    }
    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ExceptionResponse{" +
                "status='" + status + '\'' +
                ", status_code=" + status_code +
                ", message='" + message + '\'' +
                '}';
    }
}
