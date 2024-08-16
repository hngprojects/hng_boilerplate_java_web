package hng_java_boilerplate.video.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VideoWatermarkDTO {
    private MultipartFile video;
    private String watermarkText;
    private  MultipartFile watermarkImage;
    private String position;
    private int size;
    private int transparency;
}
