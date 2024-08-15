package hng_java_boilerplate.video.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class VideoCompressDto {

    private String jobId;
    private String resolution;
    private String outputFormat;
    private String bitrate;
    private byte[] video;

}
