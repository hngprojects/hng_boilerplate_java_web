package hng_java_boilerplate.image.util;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.File;

@Component
public class DirectoryInitializer {

    private final String storagePath = "/path/image/storage/";
    private final String processedPath = "/path/image/processed/";

    @PostConstruct
    public void initializeDirectories() {
        new File(storagePath).mkdirs();
        new File(processedPath).mkdirs();
    }
}
