package hng_java_boilerplate.video.service;

import hng_java_boilerplate.video.dto.DownloadableDTO;
import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.dto.VideoUploadDTO;
import hng_java_boilerplate.video.entity.VideoSuite;

import java.io.IOException;

public interface VideoService {

    VideoResponseDTO<VideoStatusDTO> videoConcat(VideoUploadDTO videoUploadDTO) throws IOException;
    VideoSuite getJob(String id);
    void addUpdateRecord(String id, String filename, int progress, String currentProcess);
    DownloadableDTO downloadVideo(String jobId) throws IOException;
}
