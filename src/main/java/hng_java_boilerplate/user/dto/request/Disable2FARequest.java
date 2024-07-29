package hng_java_boilerplate.user.dto.request;

public class Disable2FARequest {

    private String password;

    private String totp_code;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTotp_code() {
        return totp_code;
    }

    public void setTotp_code(String totp_code) {
        this.totp_code = totp_code;
    }
}
