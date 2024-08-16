package hng_java_boilerplate.video.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoConversionDto {
    private String jobId;
    private String status;
    private Map<String, String> data;
}
