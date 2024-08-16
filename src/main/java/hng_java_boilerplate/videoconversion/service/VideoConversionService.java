package hng_java_boilerplate.videoconversion.service;

import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.dto.VideoStatusDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoConversionService {
    VideoResponseDTO<VideoStatusDTO> startVideoProcess(MultipartFile video, String format) throws IOException;
}
