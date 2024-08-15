package hng_java_boilerplate.videotogif.controller;

import hng_java_boilerplate.videotogif.service.TempDirectoryService;
import hng_java_boilerplate.videotogif.service.VideoToGifService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
        File tempDir;
        File inputFile = null;
        String inputVideoPath = null;
        String outputGifPath = null;

        try {
            tempDir = tempDirectoryService.getTempDir();

            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            inputVideoPath = tempDir.getAbsolutePath() + "/" + videoFile.getOriginalFilename();
            outputGifPath = inputVideoPath.replace(".mp4", ".gif");

            inputFile = new File(inputVideoPath);
            videoFile.transferTo(inputFile);

            videoToGifService.convertVideoToGif(inputVideoPath, outputGifPath);

            return ResponseEntity.ok("GIF created successfully: " + outputGifPath);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file: " + e.getMessage());
        } catch (InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("FFmpeg process was interrupted: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A runtime error occurred: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        } finally {
            if (inputFile != null && inputFile.exists()) {
                if (!inputFile.delete()) {
                    System.err.println("Failed to delete temporary video file: " + inputFile.getAbsolutePath());
                }
            }
//
        }
    }

}
