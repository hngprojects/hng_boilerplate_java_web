package hng_java_boilerplate.video;

import hng_java_boilerplate.video.dto.DownloadableDTO;
import hng_java_boilerplate.video.exceptions.FileDoesNotExist;
import hng_java_boilerplate.video.utils.VideoUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class VideoUtilsTest {

    @Test
    public void testByteArrayResource() throws IOException {
        byte[] videoByte = "dummy video content".getBytes();
        String filename = VideoUtils.SaveVideoToFile(videoByte);

        DownloadableDTO downloadDTO = VideoUtils.byteArrayResource(filename);

        assertEquals(videoByte.length, downloadDTO.getVideoByteLength(), "The video byte length should match.");
        assertArrayEquals(videoByte, downloadDTO.getResource().getByteArray(), "The file content should match.");
    }

    @Test
    public void testByteArrayResourceFileDoesNotExist() {
        assertThrows(FileDoesNotExist.class, () -> {
            VideoUtils.byteArrayResource("nonExistentFile.mp4");
        }, "Should throw FileDoesNotExist exception when file does not exist.");
    }
}
