package hng_java_boilerplate.audio;

import hng_java_boilerplate.audio.service.AudioServiceImpl;
import hng_java_boilerplate.exception.exception_class.ServiceUnavailableException;
import hng_java_boilerplate.video.dto.VideoPathDTO;
import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.entity.VideoSuite;
import hng_java_boilerplate.video.repository.VideoRepository;
import hng_java_boilerplate.video.service.VideoServicePublisher;
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

public class AudioServiceImplTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private VideoServicePublisher videoPublisher;

    @InjectMocks
    private AudioServiceImpl audioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartAudioProcessSuccess() throws IOException {

        MultipartFile videoFile = mock(MultipartFile.class);
        when(videoFile.getBytes()).thenReturn("test video content".getBytes());
        when(videoFile.getContentType()).thenReturn("video/mp4");

        VideoSuite videoSuite = new VideoSuite();
        when(videoPublisher.sendVideo(any(VideoPathDTO.class))).thenReturn(true);
        when(videoRepository.save(any(VideoSuite.class))).thenReturn(videoSuite);


        VideoResponseDTO<VideoStatusDTO> response = audioService.startAudioProcess(videoFile, "audio/mp3");

        assertNotNull(response);
        assertEquals("Job created", response.getMessage());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertTrue(response.isSuccess());
        verify(videoPublisher).sendVideo(any(VideoPathDTO.class));
        verify(videoRepository).save(any(VideoSuite.class));
    }

    @Test
    void testStartAudioProcessFailure() throws IOException {
        MultipartFile videoFile = mock(MultipartFile.class);
        when(videoFile.getBytes()).thenReturn("test video content".getBytes());
        when(videoFile.getContentType()).thenReturn("video/mp4");

        when(videoPublisher.sendVideo(any(VideoPathDTO.class))).thenReturn(false);

        ServiceUnavailableException exception = assertThrows(ServiceUnavailableException.class, () -> {
            audioService.startAudioProcess(videoFile, "mp4");
        });

        assertEquals("Error creating job", exception.getMessage());
        verify(videoPublisher).sendVideo(any(VideoPathDTO.class));
        verify(videoRepository, never()).save(any(VideoSuite.class));
    }
}

