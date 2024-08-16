package hng_java_boilerplate.video;

import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.dto.VideoWatermarkDTO;
import hng_java_boilerplate.video.entity.VideoSuite;
import hng_java_boilerplate.video.repository.VideoRepository;
import hng_java_boilerplate.video.service.VideoServiceImpl;
import hng_java_boilerplate.video.utils.VideoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class VideoWatermarkServiceImplTest {

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private VideoServiceImpl videoServiceImpl;

    @Mock
    private VideoUtils videoUtils;

    private VideoWatermarkDTO videoWatermarkDTO;
    private byte[] videoBytes;

    @BeforeEach
    public void setUp() {
        videoWatermarkDTO = new VideoWatermarkDTO();
        videoBytes = new byte[10];
        byte[] watermarkImageBytes = new byte[5];
        byte[] watermarkedVideoBytes = new byte[15];
    }

    @Test
    public void testApplyWatermark_withImageWatermark() throws IOException {

        try (MockedStatic<VideoUtils> mockedVideoUtils = mockStatic(VideoUtils.class)) {

            MockMultipartFile videoFile = new MockMultipartFile("video", "video.mp4", "video/mp4", new byte[10]);
            MockMultipartFile watermarkImageFile = new MockMultipartFile("watermarkImage", "watermark.png", "image/png", new byte[5]);

            VideoWatermarkDTO videoWatermarkDTO = new VideoWatermarkDTO();
            videoWatermarkDTO.setVideo(videoFile);
            videoWatermarkDTO.setWatermarkImage(watermarkImageFile);

            byte[] videoBytes = new byte[10];
            byte[] watermarkImageBytes = new byte[5];
            byte[] watermarkedVideoBytes = new byte[15];


            mockedVideoUtils.when(() -> VideoUtils.videoToByte(videoWatermarkDTO.getVideo())).thenReturn(videoBytes);

            mockedVideoUtils.when(() -> VideoUtils.videoToByte(videoWatermarkDTO.getWatermarkImage())).thenReturn(watermarkImageBytes);


            mockedVideoUtils.when(() -> VideoUtils.applyImageWatermark(videoBytes, watermarkImageBytes,
                            videoWatermarkDTO.getPosition(), videoWatermarkDTO.getSize(), videoWatermarkDTO.getTransparency()))
                    .thenReturn(watermarkedVideoBytes);


            VideoResponseDTO<VideoStatusDTO> response = videoServiceImpl.applyWatermark(videoWatermarkDTO);


            assertNotNull(response);
            assertTrue(response.isSuccess());
            verify(videoRepository, times(1)).save(any(VideoSuite.class));
        }
    }

    @Test
    public void testApplyWatermark_withTextWatermark() throws IOException {

        try (MockedStatic<VideoUtils> mockedVideoUtils = mockStatic(VideoUtils.class)) {

            MockMultipartFile videoFile = new MockMultipartFile("video", "video.mp4", "video/mp4", new byte[10]);
            VideoWatermarkDTO videoWatermarkDTO = new VideoWatermarkDTO();
            videoWatermarkDTO.setVideo(videoFile);
            videoWatermarkDTO.setWatermarkText("Sample Text");

            byte[] videoBytes = new byte[10];
            byte[] watermarkedVideoBytes = new byte[15];


            mockedVideoUtils.when(() -> VideoUtils.videoToByte(videoWatermarkDTO.getVideo())).thenReturn(videoBytes);


            mockedVideoUtils.when(() -> VideoUtils.applyTextWatermark(any(), anyString(), any(), any(), any())).thenReturn(watermarkedVideoBytes);


            VideoResponseDTO<VideoStatusDTO> response = videoServiceImpl.applyWatermark(videoWatermarkDTO);


            assertNotNull(response);
            assertTrue(response.isSuccess());
            verify(videoRepository, times(1)).save(any(VideoSuite.class));
        }
    }

    @Test
    public void testApplyWatermark_withNoWatermark() throws IOException {
        try (MockedStatic<VideoUtils> mockedVideoUtils = mockStatic(VideoUtils.class)) {

            MockMultipartFile videoFile = new MockMultipartFile("video", "video.mp4", "video/mp4", videoBytes);
            videoWatermarkDTO.setVideo(videoFile);


            mockedVideoUtils.when(() -> VideoUtils.videoToByte(videoWatermarkDTO.getVideo())).thenReturn(videoBytes);


            VideoResponseDTO<VideoStatusDTO> response = videoServiceImpl.applyWatermark(videoWatermarkDTO);


            assertNotNull(response);
            assertFalse(response.isSuccess());
            assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
            verify(videoRepository, times(0)).save(any(VideoSuite.class));
        }
    }
}
