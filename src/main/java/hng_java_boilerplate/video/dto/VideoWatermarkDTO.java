package hng_java_boilerplate.video.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
public class VideoWatermarkDTO {
    private MultipartFile video;
    private String watermarkText;
    private  MultipartFile watermarkImage;
    private String position;
    private int size;
    private int transparency;
}
