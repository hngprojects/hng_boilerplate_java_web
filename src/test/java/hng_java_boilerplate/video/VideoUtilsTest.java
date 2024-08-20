package hng_java_boilerplate.video;

import hng_java_boilerplate.exception.exception_class.NotFoundException;
import hng_java_boilerplate.video.dto.DownloadableDTO;
import hng_java_boilerplate.video.utils.VideoUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class VideoUtilsTest {

    @Test
    public void testByteArrayResource() throws IOException {
        byte[] videoByte = "dummy video content".getBytes();
        String filename = VideoUtils.SaveMediaToFile(videoByte);

        DownloadableDTO downloadDTO = VideoUtils.byteArrayResource(filename);

        assertEquals(videoByte.length, downloadDTO.getVideoByteLength(), "The video byte length should match.");
        assertArrayEquals(videoByte, downloadDTO.getResource().getByteArray(), "The file content should match.");
    }

    @Test
    public void testByteArrayResourceFileDoesNotExist() {
        assertThrows(NotFoundException.class, () -> {
            VideoUtils.byteArrayResource("nonExistentFile.mp4");
        }, "Should throw FileDoesNotExist exception when file does not exist.");
    }
}
