package hng_java_boilerplate.videoconversion.serviceImpl;

import hng_java_boilerplate.exception.exception_class.ServiceUnavailableException;
import hng_java_boilerplate.video.dto.VideoPathDTO;
import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.entity.VideoSuite;
import hng_java_boilerplate.video.repository.VideoRepository;
import hng_java_boilerplate.video.service.VideoServiceImpl;
import hng_java_boilerplate.video.service.VideoServicePublisher;
import hng_java_boilerplate.video.videoEnums.JobType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VideoConversionServiceImplTest {

    @Mock
    private VideoServicePublisher videoPublisher;

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private VideoServiceImpl videoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartVideoConversion_Success() throws IOException {
        MultipartFile videoFile = mock(MultipartFile.class);
        when(videoFile.getBytes()).thenReturn("test video content".getBytes());
        when(videoFile.getContentType()).thenReturn("video/mp4");

        VideoPathDTO videoPathDTO = new VideoPathDTO();
        when(videoPublisher.sendVideo(any(VideoPathDTO.class))).thenReturn(true);
        when(videoRepository.save(any(VideoSuite.class))).thenReturn(new VideoSuite());

        VideoResponseDTO<VideoStatusDTO> response = videoService.startVideoProcess(videoFile, "video/mp4", JobType.CONVERT_VIDEO.toString());

        assertNotNull(response);
        assertEquals("Job created", response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertTrue(response.isSuccess());
        verify(videoPublisher).sendVideo(any(VideoPathDTO.class));
        verify(videoRepository).save(any(VideoSuite.class));
    }

    @Test
    void testStartVideoConversion_Failure() throws IOException {
        MultipartFile videoFile = mock(MultipartFile.class);
        when(videoFile.getBytes()).thenReturn("test video content".getBytes());
        when(videoFile.getContentType()).thenReturn("video/mp4");

        when(videoPublisher.sendVideo(any(VideoPathDTO.class))).thenReturn(false);

        ServiceUnavailableException exception = assertThrows(ServiceUnavailableException.class, () -> {
            videoService.startVideoProcess(videoFile, "video/mp4", JobType.CONVERT_VIDEO.toString());
        });

        assertEquals("Error creating job", exception.getMessage());
        verify(videoPublisher).sendVideo(any(VideoPathDTO.class));
        verify(videoRepository, never()).save(any(VideoSuite.class));
    }
}