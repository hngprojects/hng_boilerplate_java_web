package hng_java_boilerplate.SMS.dto;

public class SmsRequestDto {
    private String phone_number;
    private String message;

    public SmsRequestDto() {
    }

    public SmsRequestDto(String phone_number, String message) {
        this.phone_number = phone_number;
        this.message = message;
    }

    public String getPhone_number() {
        return phone_number;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SmsRequestDto{" +
                "phone_number='" + phone_number + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
