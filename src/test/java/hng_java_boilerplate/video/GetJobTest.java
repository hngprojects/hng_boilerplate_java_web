package hng_java_boilerplate.video;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.entity.VideoSuite;
import hng_java_boilerplate.video.exceptions.JobNotFound;
import hng_java_boilerplate.video.repository.VideoRepository;
import hng_java_boilerplate.video.utils.VideoMapper;
import hng_java_boilerplate.video.service.VideoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

public class GetJobTest {

    @InjectMocks
    private VideoServiceImpl videoService;

    @Mock
    private VideoRepository videoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetJobSuccess() {

        String jobId = UUID.randomUUID().toString();
        VideoSuite mockJob = new VideoSuite();
        when(videoRepository.findById(jobId)).thenReturn(Optional.of(mockJob));

        VideoResponseDTO<VideoStatusDTO> response = videoService.getJob(jobId.toString());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertTrue(response.isSuccess());
    }


    @Test
    public void testGetJobNotFound() {
        String jobId = UUID.randomUUID().toString();
        when(videoRepository.findById(jobId)).thenReturn(Optional.empty());

        assertThrows(JobNotFound.class, () -> videoService.getJob(jobId.toString()));
    }
}
