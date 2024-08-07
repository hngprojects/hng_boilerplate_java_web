package hng_java_boilerplate.image.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ImageProcessor {

    public static String compressAndResizeImage(MultipartFile file, String path) throws IOException {

        String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        File imageFile = new File(path + filename);


        Thumbnails.of(file.getInputStream())
                .size(800, 600) // Example size, adjust as needed
                .outputQuality(0.8) // Example compression quality
                .toFile(imageFile);

        return filename;
    }

    public static void generateDifferentSizes(String originalPath, String targetPath, String filename) throws IOException {

        String[] sizes = {"thumbnail", "medium", "large"};
        int[][] dimensions = {{150, 150}, {300, 300}, {600, 600}};

        for (int i = 0; i < sizes.length; i++) {
            Thumbnails.of(originalPath + filename)
                    .size(dimensions[i][0], dimensions[i][1])
                    .toFile(targetPath + sizes[i] + "-" + filename);
        }
    }
}
