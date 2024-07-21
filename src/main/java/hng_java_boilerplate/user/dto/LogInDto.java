package hng_java_boilerplate.user.dto;

import jakarta.validation.constraints.NotBlank;

public class LogInDto {
    @NotBlank(message = "Email and password are required.")
    private String email;
    @NotBlank(message = "Email and password are required.")
    private String password;

    public LogInDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
