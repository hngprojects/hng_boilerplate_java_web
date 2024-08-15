package hng_java_boilerplate.video.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class VideoCompressRequest {


    private MultipartFile videoFile;
    private String resolution;
    private String bitrate;
    private String outputFormat;

}
