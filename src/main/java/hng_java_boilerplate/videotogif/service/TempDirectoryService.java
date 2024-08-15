package hng_java_boilerplate.videotogif.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class TempDirectoryService {
    @Value("${temp.dir.path:temp}")
    private String tempDirPath;

    private File tempDir;

    @PostConstruct
    public void init() {
        tempDir = new File(tempDirPath);
        if (!tempDir.exists()) {
            if (!tempDir.mkdirs()) {
                throw new RuntimeException("Failed to create temp directory");
            }
        }
    }

    public File getTempDir() {
        return tempDir;
    }
}
