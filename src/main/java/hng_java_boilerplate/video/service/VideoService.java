package hng_java_boilerplate.video.service;

import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.dto.VideoUploadDTO;

import java.io.IOException;

public interface VideoService {

    VideoResponseDTO<VideoStatusDTO> videoConcat(VideoUploadDTO videoUploadDTO) throws IOException;
    VideoResponseDTO<VideoStatusDTO> getJob(String id);

}
