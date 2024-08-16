package hng_java_boilerplate.videoServiceImplTest;


import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.dto.VideoWatermarkDTO;
import hng_java_boilerplate.video.entity.VideoSuite;
import hng_java_boilerplate.video.repository.VideoRepository;
import hng_java_boilerplate.video.service.VideoServiceImpl;
import hng_java_boilerplate.video.utils.VideoUtils;
import hng_java_boilerplate.video.videoEnums.VideoStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class VideoServiceImplTest {

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private VideoServiceImpl videoServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testApplyWatermark_Success() throws IOException {

        MultipartFile video = new MockMultipartFile("video.mp4", "video.mp4", "video/mp4", new ByteArrayInputStream("video content".getBytes()));
        MultipartFile watermark = new MockMultipartFile("watermark.png", "watermark.png", "image/png", new ByteArrayInputStream("watermark content".getBytes()));
        VideoWatermarkDTO videoWatermarkDTO = new VideoWatermarkDTO();
        videoWatermarkDTO.setVideo(video);
        videoWatermarkDTO.setWatermark(watermark);
        videoWatermarkDTO.setPosition("top-left");
        videoWatermarkDTO.setSize(50);
        videoWatermarkDTO.setTransparency(5);

        VideoSuite videoSuite = new VideoSuite();
        videoSuite.setJobId("12345");
        videoSuite.setStatus(VideoStatus.PENDING.toString());

        when(VideoUtils.videoToByte(video)).thenReturn("video content".getBytes());
        when(VideoUtils.videoToByte(watermark)).thenReturn("watermark content".getBytes());
        when(VideoUtils.applyWatermark("video content".getBytes(), "watermark content".getBytes(), "top-left", 50, 5))
                .thenReturn("watermarked video content".getBytes());
        when(videoRepository.save(videoSuite)).thenReturn(videoSuite);


        VideoResponseDTO<VideoStatusDTO> response = videoServiceImpl.applyWatermark(videoWatermarkDTO);


        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        assertEquals("Watermark applied successfully", response.getMessage());
        assertEquals(true, response.isSuccess());
    }

    @Test
    public void testApplyWatermark_Failure() throws IOException {
        MultipartFile video = new MockMultipartFile("video.mp4", "video.mp4", "video/mp4", new ByteArrayInputStream("video content".getBytes()));
        MultipartFile watermark = new MockMultipartFile("watermark.png", "watermark.png", "image/png", new ByteArrayInputStream("watermark content".getBytes()));
        VideoWatermarkDTO videoWatermarkDTO = new VideoWatermarkDTO();
        videoWatermarkDTO.setVideo(video);
        videoWatermarkDTO.setWatermark(watermark);
        videoWatermarkDTO.setPosition("top-left");
        videoWatermarkDTO.setSize(50);
        videoWatermarkDTO.setTransparency(5); //

        when(VideoUtils.videoToByte(video)).thenReturn("video content".getBytes());
        when(VideoUtils.videoToByte(watermark)).thenReturn("watermark content".getBytes());
        when(VideoUtils.applyWatermark("video content".getBytes(), "watermark content".getBytes(), "top-left", 50, 5))
                .thenThrow(new IOException("Error applying watermark"));


        assertThrows(IOException.class, () -> videoServiceImpl.applyWatermark(videoWatermarkDTO));
    }
}