package hng_java_boilerplate.user.dto.response;

import java.util.Map;

public class VerifyTwoFactorAuthResponse {

    private String status_code;

    private String message;

    private Map<String, Object> data;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }


}
