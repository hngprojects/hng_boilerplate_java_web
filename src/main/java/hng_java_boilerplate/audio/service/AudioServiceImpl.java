package hng_java_boilerplate.audio.service;

import hng_java_boilerplate.exception.exception_class.ServiceUnavailableException;
import hng_java_boilerplate.video.dto.VideoPathDTO;
import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.entity.VideoSuite;
import hng_java_boilerplate.video.repository.VideoRepository;
import hng_java_boilerplate.video.service.VideoServicePublisher;
import hng_java_boilerplate.video.utils.VideoMapper;
import hng_java_boilerplate.video.utils.VideoUtils;
import hng_java_boilerplate.video.videoEnums.JobType;
import hng_java_boilerplate.video.videoEnums.VideoMessage;
import hng_java_boilerplate.video.videoEnums.VideoStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AudioServiceImpl implements AudioService {

    private final VideoRepository videoRepository;
    private final VideoServicePublisher  videoPublisher;
    @Override
    public VideoResponseDTO<VideoStatusDTO> startAudioProcess(MultipartFile video, String outputFormat) throws IOException {
        String jobId = VideoUtils.generateUuid();

        VideoSuite job = VideoUtils.videoSuite(jobId, VideoStatus.PENDING.toString(), null,
                JobType.EXTRACT_AUDIO.toString(), VideoMessage.PENDING.toString(),
                VideoStatus.PENDING.toString(), video.getContentType(),
                outputFormat);
        VideoPathDTO videoPathDTO = new VideoPathDTO();
        videoPathDTO.setJobId(jobId);
        videoPathDTO.addVideo("video1", video.getBytes());

        if (videoPublisher.sendVideo(videoPathDTO)){
            return VideoUtils.response("Job created", HttpStatus.CREATED.value(), true,
                    VideoMapper.INSTANCE.toDTO(videoRepository.save(job)));
        }
        throw new ServiceUnavailableException("Error creating job");
    }

    @Override
    public VideoSuite checkAudioProcess(String jobId) {
        return null;
    }
}
