package hng_java_boilerplate.video.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VideoStatusDTO {

    private String jobId;
    private String status;
    private String message;
    private String filename;
    private String jobType;
    private int progress;
    private String currentProcess;
    private String mediaFormat;
    private String expectedFormat;
}
