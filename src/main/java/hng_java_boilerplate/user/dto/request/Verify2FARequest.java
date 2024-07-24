package hng_java_boilerplate.user.dto.request;

public class Verify2FARequest {

    private String totp_code;

    public String getTotp_code() {
        return totp_code;
    }

    public void setTotp_code(String totp_code) {
        this.totp_code = totp_code;
    }
}
