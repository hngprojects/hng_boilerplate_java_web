package hng_java_boilerplate.util;

public enum ConstantMessages {
    SUCCESS("success", 1),
    UNSUCCESSFUL("unsuccessful", 2),
    SMS_SENT_SUCCESSFULLY("SMS sent successfully.", 3),
    INVALID_PHONE_NUMBER_OR_CONTENT("Valid phone number and message content must be provided.",4),
    FAILED("Failed to send SMS. Please try again later.",5),
    INTERNAL_ERROR("Internal error.", 6);

    public String message;
    public int status;
    ConstantMessages(String message, int status){
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ConstantMessages{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
