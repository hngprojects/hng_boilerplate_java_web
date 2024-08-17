package hng_java_boilerplate.video;

import hng_java_boilerplate.video.dto.VideoCompressDto;
import hng_java_boilerplate.video.dto.VideoCompressRequest;
import hng_java_boilerplate.video.dto.VideoPathDTO;
import hng_java_boilerplate.video.entity.VideoSuite;
import hng_java_boilerplate.video.repository.VideoRepository;
import hng_java_boilerplate.video.service.VideoServiceImpl;
import hng_java_boilerplate.video.service.VideoServicePublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CompressVideoTest {

    @InjectMocks
    private VideoServiceImpl videoService;

    @Mock
    private VideoServicePublisher publisher;

    @Mock
    private VideoRepository videoRepository;

    private VideoSuite suite;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        suite = new VideoSuite();
        suite.setFilename("fileName");
        suite.setJobId("jobId");
        suite.setMessage("message");
        suite.setProgress(30);
        suite.setCurrentProcess("Pending");
        suite.setExpectedFormat("expectedFormat");
        suite.setStatus("Pending");
        suite.setJobType("Compress video");

        videoService = spy(new VideoServiceImpl(publisher, videoRepository));
        doNothing().when(videoService).validateCompressionRequest(any(VideoCompressRequest.class));

    }

    @Test
    public void compress_Video_Test() throws IOException {
        VideoCompressDto videoCompressDto = VideoCompressDto.builder().bitrate("LOW").resolution("LOW").jobId("jobId")
                .outputFormat("mp4").video(new byte[]{}).build();

        when(publisher.sendVideo(any(VideoPathDTO.class))).thenReturn(true);

        when(videoRepository.save(any(VideoSuite.class))).thenReturn(suite);

        VideoCompressRequest request = VideoCompressRequest.builder().bitrate("HIGH").outputFormat("mp4")
                .videoFile(new MockMultipartFile("name", new byte[]{})).resolution("LOW").build();

        var response = videoService.compressVideo(request);
        assertNotNull(response);
    }
}
