package hng_java_boilerplate.video.service;

import hng_java_boilerplate.video.dto.*;
import hng_java_boilerplate.video.exceptions.FileDoesNotExist;
import hng_java_boilerplate.video.exceptions.JobCreationError;
import hng_java_boilerplate.video.exceptions.JobNotFound;
import hng_java_boilerplate.video.utils.VideoMapper;
import hng_java_boilerplate.video.videoEnums.JobType;
import hng_java_boilerplate.video.videoEnums.VideoMessage;
import hng_java_boilerplate.video.videoEnums.VideoStatus;
import hng_java_boilerplate.video.entity.VideoSuite;
import hng_java_boilerplate.video.repository.VideoRepository;
import hng_java_boilerplate.video.utils.VideoUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static hng_java_boilerplate.video.utils.VideoUtils.*;

@RequiredArgsConstructor
@Service
public class VideoServiceImpl implements VideoService{
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);

    private final VideoServicePublisher publisher;
    private final VideoRepository videoRepository;

    @Override
    public VideoResponseDTO<VideoStatusDTO> startVideoProcess(MultipartFile video, String outputFormat, String jobType) throws IOException {
        String jobId = VideoUtils.generateUuid();
        VideoSuite job = VideoUtils.videoSuite(jobId, VideoStatus.PENDING.toString(), null,
                jobType, VideoMessage.PENDING.toString(),
                VideoStatus.PENDING.toString(), video.getContentType(),
                outputFormat);
        VideoPathDTO videoPathDTO = new VideoPathDTO();
        videoPathDTO.setJobId(jobId);
        videoPathDTO.addVideo("video1", video.getBytes());

        if(publisher.sendVideo(videoPathDTO)){
            return VideoUtils.response("Job created", HttpStatus.CREATED.value(), true,
                    VideoMapper.INSTANCE.toDTO(videoRepository.save(job)));
        }
        throw new JobCreationError("Error creating job");
    }


    @Override
    public VideoResponseDTO<VideoStatusDTO> videoConcat(VideoUploadDTO videoUploadDTO) throws IOException {
        VideoPathDTO videoPathDTO = new VideoPathDTO();
        VideoSuite videoSuite;
        String jobId = VideoUtils.generateUuid();

        videoPathDTO.setJobId(jobId);
        int count = 1;
        for(MultipartFile video : videoUploadDTO.getVideos()){
            videoPathDTO.addVideo("video"+count, VideoUtils.videoToByte(video));
            count += 1;
        }

        if(publisher.sendVideo(videoPathDTO)){
           videoSuite = VideoUtils.videoSuite(jobId, VideoStatus.PENDING.toString(), null,
                    JobType.MERGE_VIDEO.toString(), VideoMessage.PENDING.toString(),
                   VideoStatus.PENDING.toString(), null, "video/mp4");

            return VideoUtils.response("Job created", HttpStatus.CREATED.value(), true,
                    VideoMapper.INSTANCE.toDTO(videoRepository.save(videoSuite)));
        }

        throw new JobCreationError("Error creating job");

    }

    @Override
    public VideoSuite getJob(String id) {
        VideoSuite job = videoRepository.findById(id)
                .orElseThrow(() -> new JobNotFound("Job doesn't exist"));
        return job;
    }

    @Override
    public void addUpdateRecord(String id, String filename, int progress, String currentProcess) {
        videoRepository.updateJob(id, filename, progress, currentProcess );
    }

    @Override
    public DownloadableDTO downloadVideo(String jobId) throws IOException {
        VideoSuite job = videoRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFound("Job doesn't exist"));
        if(job.getFilename() == null)
            throw new FileNotFoundException("This file is not ready for download");

        DownloadableDTO downloadableDTO = VideoUtils.byteArrayResource(job.getFilename());
        downloadableDTO.setContentType(job.getExpectedFormat());
        return downloadableDTO;
    }

    @Override
    public DownloadableDTO downloadCompressedVideo(String jobId) throws IOException {
        VideoSuite job = videoRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFound("Job does not exist"));
        if(job.getFilename() == null)
            throw new FileNotFoundException("This file is not ready for download");

        DownloadableDTO downloadableDTO = VideoUtils.byteArrayResource(job.getFilename());
        downloadableDTO.setContentType(job.getExpectedFormat());
        return downloadableDTO;
    }

    @Override
    public Map<String, String> getFileSize(String jobId) {
        VideoSuite job = videoRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFound("Job doesn't exist"));
        Map<String, String> data = new HashMap<>();
        data.put("originalFileSize", job.getOriginalFileSize());
        data.put("compressedFileSize", job.getCompressedFileSize());
        return data;
    }

    @Override
    public VideoCompressResponse<?> compressVideo(VideoCompressRequest request) throws IOException {
        validateCompressionRequest(request);
        VideoSuite videoSuite;
        String jobId = VideoUtils.generateUuid();
        String originalFileSize = VideoUtils.formatFileSize(request.getVideoFile().getSize());

        String originalFilename = request.getVideoFile().getOriginalFilename();
        assert originalFilename != null;
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        Map<String, byte[]> video = new HashMap<>();
        video.put("video", VideoUtils.videoToByte(request.getVideoFile()));
        VideoPathDTO videoPathDTO = new VideoPathDTO(jobId,video);
        boolean status = publisher.sendVideo(videoPathDTO);
        if (status) {
            videoSuite = VideoUtils.videoSuite(jobId, VideoStatus.PENDING.toString(), request.getVideoFile().getOriginalFilename(), JobType.COMPRESS_VIDEO.toString(), String.valueOf(VideoMessage.PENDING), VideoStatus.PENDING.toString(), fileExtension, request.getOutputFormat());
            videoSuite.setBitrate(request.getBitrate());
            videoSuite.setResolution(request.getResolution());
            videoSuite.setOriginalFileSize(originalFileSize);
            videoRepository.save(videoSuite);
            VideoStatusDTO response = new VideoStatusDTO(jobId, PENDING, VideoMessage.PENDING.getStatus(), request.getVideoFile().getOriginalFilename(), "compress video", 0, PENDING, fileExtension, request.getOutputFormat());
            return VideoCompressResponse.builder().message("Job Created").statusCode(HttpStatus.CREATED.value()).data(response).build();
        }

        throw new JobCreationError("Error creating job");
    }

    public void validateCompressionRequest(VideoCompressRequest request) {
        MultipartFile videoFile = request.getVideoFile();
        if (videoFile == null || videoFile.isEmpty()) {
            throw new IllegalArgumentException("Video file is required and cannot be empty.");
        }

        String filename = videoFile.getOriginalFilename();
        if (filename == null || !isValidVideoFormat(filename)) {
            throw new IllegalArgumentException("Invalid video format. Accepted formats are: .mp4, .avi");
        }

        List<String> validOutputFormats = Arrays.asList("mp4", "avi");
        if (!validOutputFormats.contains(request.getOutputFormat())) {
            throw new IllegalArgumentException("Invalid output format. Valid options are mp4, avi.");
        }
    }

    private boolean isValidVideoFormat(String filename) {
        List<String> validFormats = Arrays.asList(".mp4", ".avi", ".mkv", ".mov");
        String fileExtension = filename.substring(filename.lastIndexOf('.')).toLowerCase();
        return validFormats.contains(fileExtension);
    }


}
