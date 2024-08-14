package hng_java_boilerplate.video.service;

import hng_java_boilerplate.video.dto.*;
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

import java.io.FileNotFoundException;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class VideoServiceImpl implements VideoService{
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);

    private final VideoServicePublisher publisher;
    private final VideoRepository videoRepository;

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

        if(publisher.sendVideoConcat(videoPathDTO)){
           videoSuite = VideoUtils.videoSuite(jobId, VideoStatus.PENDING.toString(), null,
                    VideoJobType.MERGE_VIDEO.toString(), VideoMessage.PENDING.toString(),
                   VideoStatus.PENDING.toString(), null, null);

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
}
