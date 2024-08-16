package hng_java_boilerplate.video.controller;
import hng_java_boilerplate.video.service.AudioReplacementService;
import hng_java_boilerplate.video.utils.SupportedFormats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/audio-replacement")
@PreAuthorize("hasRole('ADMIN')")
public class AudioReplacementController {

    @Autowired
    private AudioReplacementService audioReplacementService;

    @PostMapping("/replace-audio")
    public ResponseEntity<String> replaceAudio(@RequestParam("videoFile") MultipartFile videoFile,
                                               @RequestParam("audioFile") MultipartFile audioFile) {
        try {

            String videoExtension = getFileExtension(Objects.requireNonNull(videoFile.getOriginalFilename()));
            if (!SupportedFormats.VIDEO_FORMATS.contains(videoExtension.toLowerCase())) {
                return ResponseEntity.badRequest().body("Unsupported video format: " + videoExtension);
            }


            Path videoPath = Paths.get("uploads/" + videoFile.getOriginalFilename());
            Files.write(videoPath, videoFile.getBytes());

            Path audioPath = Paths.get("uploads/" + audioFile.getOriginalFilename());
            Files.write(audioPath, audioFile.getBytes());

            String outputFilePath = "uploads/output-" + videoFile.getOriginalFilename();

            audioReplacementService.replaceAudio(videoPath.toString(), audioPath.toString(), outputFilePath);

            return ResponseEntity.ok("Audio replaced successfully. Output file: " + outputFilePath);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error replacing audio: " + e.getMessage());
        }
    }
    @PostMapping("/adjust-sync")
    public ResponseEntity<String> adjustAudioSync(@RequestParam("videoFile") MultipartFile videoFile,
                                                  @RequestParam("audioFile") MultipartFile audioFile,
                                                  @RequestParam("offset") double offsetSeconds) {
        try {
            String videoExtension = getFileExtension(Objects.requireNonNull(videoFile.getOriginalFilename()));
            if (!SupportedFormats.VIDEO_FORMATS.contains(videoExtension.toLowerCase())) {
                return ResponseEntity.badRequest().body("Unsupported video format: " + videoExtension);
            }
            Path videoPath = Paths.get("uploads/" + videoFile.getOriginalFilename());
            Files.write(videoPath, videoFile.getBytes());

            Path audioPath = Paths.get("uploads/" + audioFile.getOriginalFilename());
            Files.write(audioPath, audioFile.getBytes());

            String outputFilePath = "uploads/synced-output-" + videoFile.getOriginalFilename();

            audioReplacementService.adjustAudioSync(videoPath.toString(), audioPath.toString(), outputFilePath, offsetSeconds);

            return ResponseEntity.ok("Audio sync adjusted successfully. Output file: " + outputFilePath);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adjusting audio sync: " + e.getMessage());
        }
    }


    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
