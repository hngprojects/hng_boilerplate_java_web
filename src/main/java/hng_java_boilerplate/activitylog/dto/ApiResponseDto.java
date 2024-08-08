package hng_java_boilerplate.activitylog.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@NoArgsConstructor
public class ApiResponseDto {
    private String message;
    private boolean success;
    private int statusCode;
    private List<ActivityLogResponseDto> data;

    public ApiResponseDto(String message, boolean success, int statusCode, List<ActivityLogResponseDto> data) {
        this.message = message;
        this.success = success;
        this.statusCode = statusCode;
        this.data = data;
    }

}
