package hng_java_boilerplate.organisation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiResponseDTO {
    @JsonProperty("status_code")
    private int statusCode;
    private String message;
    private Object data;
}