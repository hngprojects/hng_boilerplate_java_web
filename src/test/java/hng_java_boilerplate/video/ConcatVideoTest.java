package hng_java_boilerplate.video;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hng_java_boilerplate.video.dto.VideoPathDTO;
import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.dto.VideoUploadDTO;
import hng_java_boilerplate.video.entity.VideoSuite;
import hng_java_boilerplate.video.exceptions.JobCreationError;
import hng_java_boilerplate.video.repository.VideoRepository;
import hng_java_boilerplate.video.service.VideoServiceImpl;
import hng_java_boilerplate.video.service.VideoServicePublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

public class ConcatVideoTest {

    @InjectMocks
    private VideoServiceImpl videoService;

    @Mock
    private VideoServicePublisher publisher;

    @Mock
    private VideoRepository videoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testVideoConcatSuccess() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenReturn(new byte[0]);
        VideoUploadDTO videoUploadDTO = new VideoUploadDTO(Collections.singletonList(mockFile));
        UUID jobId = UUID.randomUUID();
        when(publisher.sendVideo(any(VideoPathDTO.class))).thenReturn(true);
        when(videoRepository.save(any(VideoSuite.class))).thenReturn(new VideoSuite());

        VideoResponseDTO<VideoStatusDTO> response = videoService.videoConcat(videoUploadDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertTrue(response.isSuccess());
    }

    @Test
    public void testVideoConcatFailure() throws IOException {

        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenReturn(new byte[0]); // Simulate video file bytes
        VideoUploadDTO videoUploadDTO = new VideoUploadDTO(Collections.singletonList(mockFile));
        when(publisher.sendVideo(any(VideoPathDTO.class))).thenReturn(false);

        assertThrows(JobCreationError.class, () -> videoService.videoConcat(videoUploadDTO));
    }
}

