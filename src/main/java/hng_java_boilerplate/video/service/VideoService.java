package hng_java_boilerplate.video.service;

import hng_java_boilerplate.video.dto.*;
import hng_java_boilerplate.video.entity.VideoSuite;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoService {

    VideoResponseDTO<VideoStatusDTO> videoConcat(VideoUploadDTO videoUploadDTO) throws IOException;
    VideoSuite getJob(String id);
    void addUpdateRecord(String id, String filename, int progress, String currentProcess);

    VideoResponseDTO<VideoStatusDTO> applyWatermark(VideoWatermarkDTO videoWatermarkDTO) throws IOException;


    DownloadableDTO downloadVideo(String jobId) throws IOException;
    VideoResponseDTO<VideoStatusDTO> startVideoProcess(MultipartFile video, String format, String jobType) throws IOException;
}
