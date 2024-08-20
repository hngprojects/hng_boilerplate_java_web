package hng_java_boilerplate.video.service;

import hng_java_boilerplate.exception.exception_class.InternalServerException;
import hng_java_boilerplate.exception.exception_class.NotFoundException;
import hng_java_boilerplate.exception.exception_class.ServiceUnavailableException;
import hng_java_boilerplate.video.dto.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);

    private final VideoServicePublisher publisher;
    private final VideoRepository videoRepository;

    @Override
    public VideoResponseDTO<VideoStatusDTO> startVideoProcess(MultipartFile video, String outputFormat, String jobType) {
        String jobId = VideoUtils.generateUuid();
        VideoSuite job = VideoUtils.videoSuite(jobId, VideoStatus.PENDING.toString(), null,
                jobType, VideoMessage.PENDING.toString(),
                VideoStatus.PENDING.toString(), video.getContentType(),
                outputFormat);
        VideoPathDTO videoPathDTO = new VideoPathDTO();
        videoPathDTO.setJobId(jobId);

        try {
            videoPathDTO.addVideo("video1", video.getBytes());

            if (publisher.sendVideo(videoPathDTO)) {
                return VideoUtils.response("Job created", HttpStatus.CREATED.value(), true,
                        VideoMapper.INSTANCE.toDTO(videoRepository.save(job)));
            }
            throw new ServiceUnavailableException("Error creating job");
        } catch (IOException ex) {
            throw new InternalServerException("An error occurred while processing video file");
        }
    }

    @Override
    public VideoResponseDTO<VideoStatusDTO> videoConcat(VideoUploadDTO videoUploadDTO) {
        VideoPathDTO videoPathDTO = new VideoPathDTO();
        VideoSuite videoSuite;
        String jobId = VideoUtils.generateUuid();

        videoPathDTO.setJobId(jobId);
        int count = 1;
        try {
            for (MultipartFile video : videoUploadDTO.getVideos()) {
                videoPathDTO.addVideo("video" + count, VideoUtils.videoToByte(video));
                count += 1;
            }

            if (publisher.sendVideo(videoPathDTO)) {
                videoSuite = VideoUtils.videoSuite(jobId, VideoStatus.PENDING.toString(), null,
                        JobType.MERGE_VIDEO.toString(), VideoMessage.PENDING.toString(),
                        VideoStatus.PENDING.toString(), null, "video/mp4");

                return VideoUtils.response("Job created", HttpStatus.CREATED.value(), true,
                        VideoMapper.INSTANCE.toDTO(videoRepository.save(videoSuite)));
            }

            throw new ServiceUnavailableException("Error creating job");
        } catch (IOException ex) {
            throw new InternalServerException("An error occurred trying to process file");
        }
    }

    @Override
    public VideoSuite getJob(String id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Job doesn't exist"));
    }

    @Override
    public void addUpdateRecord(String id, String filename, int progress, String currentProcess) {
        videoRepository.updateJob(id, filename, progress, currentProcess );
    }

    @Override
    public DownloadableDTO downloadVideo(String jobId) {
        VideoSuite job = videoRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job doesn't exist"));
        if (job.getFilename() == null)
            throw new NotFoundException("This file is not ready for download");

        try {
            DownloadableDTO downloadableDTO = VideoUtils.byteArrayResource(job.getFilename());
            downloadableDTO.setContentType(job.getExpectedFormat());
            return downloadableDTO;
        } catch (IOException ex) {
            throw new InternalServerException("An error occurred while processing file");
        }
    }
}
