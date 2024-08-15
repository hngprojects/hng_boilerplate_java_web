package hng_java_boilerplate.audio.service;

import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.entity.VideoSuite;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AudioService {

    VideoResponseDTO<VideoStatusDTO> startAudioProcess(MultipartFile video, String outputFormat) throws IOException;
    VideoSuite checkAudioProcess(String jobId);
}
