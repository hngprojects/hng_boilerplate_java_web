package hng_java_boilerplate.payment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Map;

@JsonPropertyOrder({"status_code", "message", "data"})
public record SessionResponse (

        String message,

        @JsonProperty("status_code")
        int StatusCode,

        Map<String, String> data
){
}
