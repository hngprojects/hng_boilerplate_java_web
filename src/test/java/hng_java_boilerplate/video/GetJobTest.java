package hng_java_boilerplate.video;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hng_java_boilerplate.video.dto.DownloadableDTO;
import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.entity.VideoSuite;
import hng_java_boilerplate.video.exceptions.JobNotFound;
import hng_java_boilerplate.video.repository.VideoRepository;
import hng_java_boilerplate.video.utils.VideoMapper;
import hng_java_boilerplate.video.service.VideoServiceImpl;
import hng_java_boilerplate.video.utils.VideoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;

import java.io.IOException;
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

        VideoSuite response = videoService.getJob(jobId.toString());

        assertNotNull(response);
    }


    @Test
    public void testGetJobNotFound() {
        String jobId = UUID.randomUUID().toString();
        when(videoRepository.findById(jobId)).thenReturn(Optional.empty());

        assertThrows(JobNotFound.class, () -> videoService.getJob(jobId.toString()));
    }

    @Test
    public void testAddUpdateRecord() {
        doNothing().when(videoRepository).updateJob("123", "testVideo.mp4", 50, "PROCESSING");

        videoService.addUpdateRecord("123", "testVideo.mp4", 50, "PROCESSING");

        verify(videoRepository, times(1)).updateJob("123", "testVideo.mp4", 50, "PROCESSING");
    }

    @Test
    public void testDownloadVideo() throws IOException {
        VideoSuite testVideoSuite = new VideoSuite();
        testVideoSuite.setJobId("123");
        testVideoSuite.setFilename("testVideo.mp4");

        DownloadableDTO expectedDownloadableDTO = new DownloadableDTO();
        expectedDownloadableDTO.setVideoByteLength(234);
        expectedDownloadableDTO.setResource(new ByteArrayResource("testVideo.mp4".getBytes()));

        try (MockedStatic<VideoUtils> mockedVideoUtils = mockStatic(VideoUtils.class)) {
            mockedVideoUtils.when(() -> VideoUtils.byteArrayResource("testVideo.mp4")).thenReturn(expectedDownloadableDTO);

            when(videoRepository.findById("123")).thenReturn(Optional.of(testVideoSuite));

            DownloadableDTO downloadableDTO = videoService.downloadVideo("123");

            assertNotNull(downloadableDTO, "DownloadableDTO should not be null.");
            assertEquals(expectedDownloadableDTO.getVideoByteLength(), downloadableDTO.getVideoByteLength(), "The video byte length should match.");
            assertArrayEquals(expectedDownloadableDTO.getResource().getByteArray(), downloadableDTO.getResource().getByteArray(), "The file content should match.");
        }
    }

    @Test
    public void testDownloadVideoFileNotFound() {
        VideoSuite testVideoSuite = new VideoSuite();
        testVideoSuite.setJobId("123");
        testVideoSuite.setFilename("testVideo.mp4");

        when(videoRepository.findById("123")).thenReturn(Optional.of(testVideoSuite));
        testVideoSuite.setFilename(null);

        assertThrows(IOException.class, () -> {
            videoService.downloadVideo("123");
        }, "Should throw FileNotFoundException when filename is null.");
    }

    @Test
    public void testDownloadVideoJobNotFound() {
        when(videoRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(JobNotFound.class, () -> {
            videoService.downloadVideo("123");
        }, "Should throw JobNotFound exception when job does not exist.");
    }


}
