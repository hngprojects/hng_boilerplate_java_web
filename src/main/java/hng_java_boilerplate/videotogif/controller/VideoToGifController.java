package hng_java_boilerplate.videotogif.controller;

import hng_java_boilerplate.videotogif.service.TempDirectoryService;
import hng_java_boilerplate.videotogif.service.VideoToGifService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/convert")
@RequiredArgsConstructor
public class VideoToGifController {

    private final VideoToGifService videoToGifService;
    private final TempDirectoryService tempDirectoryService;

    @PostMapping("/video-to-gif")
    public ResponseEntity<String> convertVideoToGif(@RequestParam("file") MultipartFile videoFile) {
        try {
            // Get the temporary directory
            File tempDir = tempDirectoryService.getTempDir();

            // Define input and output paths
            String inputVideoPath = tempDir.getAbsolutePath() + "/" + videoFile.getOriginalFilename();
            String outputGifPath = inputVideoPath.replace(".mp4", ".gif");

            // Save the uploaded video file to the temporary directory
            File inputFile = new File(inputVideoPath);
            videoFile.transferTo(inputFile);

            // Convert the video to GIF
            videoToGifService.convertVideoToGif(inputVideoPath, outputGifPath);

            // Return the path to the generated GIF
            return ResponseEntity.ok("GIF created successfully: " + outputGifPath);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting video to GIF: " + e.getMessage());
        }
    }
}
