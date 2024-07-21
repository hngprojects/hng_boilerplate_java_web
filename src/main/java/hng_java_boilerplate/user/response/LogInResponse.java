package hng_java_boilerplate.user.response;

public class LogInResponse {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LogInResponse(String token) {
        this.token = token;
    }

    public LogInResponse() {
    }
}
