package hng_java_boilerplate.video.service;

import hng_java_boilerplate.video.dto.VideoPathDTO;
import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.dto.VideoUploadDTO;
import hng_java_boilerplate.video.exceptions.JobCreationError;
import hng_java_boilerplate.video.exceptions.JobNotFound;
import hng_java_boilerplate.video.utils.VideoMapper;
import hng_java_boilerplate.video.videoEnums.VideoJobType;
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
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class VideoServiceImpl implements VideoService{
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);

    private final VideoServicePublisher publisher;
    private final VideoRepository videoRepository;

    @Override
    public VideoResponseDTO<VideoStatusDTO> videoConcat(VideoUploadDTO videoUploadDTO) throws IOException {
        VideoPathDTO videoPathDTO = new VideoPathDTO();
        VideoStatusDTO videoStatusDTO = new VideoStatusDTO();
        VideoSuite videoSuite;
        String jobId = VideoUtils.generateUuid();

        videoPathDTO.setJobId(jobId);
        int count = 1;
        for(MultipartFile video : videoUploadDTO.getVideos()){
            videoPathDTO.addVideo("video"+count, VideoUtils.videoToByte(video));
            count += 1;
        }

        if(publisher.sendVideoConcat(videoPathDTO)){
           videoSuite = VideoUtils.videoSuite(jobId, VideoStatus.PENDING.toString(), null,
                    VideoJobType.MERGE_VIDEO.toString(), VideoMessage.PROCESSING.toString());

            return VideoUtils.response("Job created", HttpStatus.CREATED.value(), true,
                    VideoMapper.INSTANCE.toDTO(videoRepository.save(videoSuite)));
        }

        throw new JobCreationError("Error creating job");

    }

    @Override
    public VideoResponseDTO<VideoStatusDTO> getJob(String id) {

        VideoSuite job = videoRepository.findById(id)
                .orElseThrow(() -> new JobNotFound("Job doesn't exist"));

        return VideoUtils.response("Found", HttpStatus.OK.value(), true,
                    VideoMapper.INSTANCE.toDTO(videoRepository.save(job)));
    }
}
