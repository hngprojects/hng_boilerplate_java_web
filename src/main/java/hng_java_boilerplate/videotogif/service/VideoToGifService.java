package hng_java_boilerplate.videotogif.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
@Service
public class VideoToGifService {
    public void convertVideoToGif(String inputVideoPath, String outputGifPath) throws IOException, InterruptedException {
        // FFmpeg command to convert video to GIF
        String[] command = {
                "ffmpeg",
                "-i", inputVideoPath,       // Input file
                "-vf", "fps=10,scale=320:-1:flags=lanczos", // Filter for frames per second and scaling
                "-c:v", "gif",              // GIF format
                "-y",                        // Overwrite output file if exists
                outputGifPath               // Output file
        };

        // Using ProcessBuilder to run the FFmpeg command
        ProcessBuilder processBuilder = new ProcessBuilder(command);

        // Set the working directory or environment variables if needed
        processBuilder.directory(new File(System.getProperty("user.dir")));

        // Start the process
        Process process = processBuilder.start();

        // Wait for the process to complete
        boolean finished = process.waitFor(60, TimeUnit.SECONDS);

        if (!finished) {
            throw new RuntimeException("FFmpeg process timed out!");
        }

        // Check if the process completed successfully
        if (process.exitValue() != 0) {
            throw new RuntimeException("FFmpeg process failed with exit code " + process.exitValue());
        }
    }
}
