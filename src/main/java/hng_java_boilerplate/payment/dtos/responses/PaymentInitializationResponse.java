package hng_java_boilerplate.payment.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PaymentInitializationResponse {

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
