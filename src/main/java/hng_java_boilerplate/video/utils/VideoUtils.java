package hng_java_boilerplate.video.utils;

import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.dto.VideoUploadDTO;
import hng_java_boilerplate.video.entity.VideoSuite;
import hng_java_boilerplate.video.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class VideoUtils {


    public static String UPLOAD_DIR = "uploads/tempFolder";
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);

    public static String saveVideoTemp(MultipartFile file) {

        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
            }

            Path filePath = Paths.get(UPLOAD_DIR, file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            return filePath.toAbsolutePath().toString();

        } catch (IOException e) {
            logger.error("Error saving file: {}", e.getMessage(), e);
            return "File upload failed: " + e.getMessage();
        }
    }

    public static String generateUuid(){
        return UUID.randomUUID().toString();
    }

    public static VideoSuite videoSuite(String jobId, String status,
                                        String outputVideoUrl, String jobType, String message){
        VideoSuite videoSuite = new VideoSuite();

        videoSuite.setJobId(jobId);
        videoSuite.setStatus(status);
        videoSuite.setOutputVideoUrl(outputVideoUrl);
        videoSuite.setJobType(jobType);
        videoSuite.setMessage(message);
        return videoSuite;
    }

    public static byte[] videoToByte(MultipartFile video) throws IOException {
        return video.getBytes();
    }

    public static VideoResponseDTO<VideoStatusDTO> response (String message, int statusCode,
                                                boolean success, VideoStatusDTO statusDTO){

        VideoResponseDTO<VideoStatusDTO> responseDTO = new VideoResponseDTO<>();
        responseDTO.setMessage(message);
        responseDTO.setSuccess(success);
        responseDTO.setStatusCode(statusCode);
        responseDTO.setData(statusDTO);
        return responseDTO;
    }
}
