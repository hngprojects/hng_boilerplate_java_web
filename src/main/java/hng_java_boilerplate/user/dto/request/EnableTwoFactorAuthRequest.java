package hng_java_boilerplate.user.dto.request;


public class EnableTwoFactorAuthRequest {

    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
