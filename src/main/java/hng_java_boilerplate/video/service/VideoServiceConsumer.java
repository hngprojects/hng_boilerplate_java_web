package hng_java_boilerplate.video.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.email.entity.EmailMessage;
import hng_java_boilerplate.video.dto.VideoPathDTO;
import hng_java_boilerplate.video.utils.VideoUtils;
import hng_java_boilerplate.video.videoEnums.VideoStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

import static hng_java_boilerplate.video.utils.VideoUtils.UPLOAD_DIR;

@RequiredArgsConstructor
@Service
@Slf4j
public class VideoServiceConsumer {

    private final ObjectMapper objectMapper;
    private final VideoService videoService;

    @RabbitListener(queues = "${rabbitmq.queue.concat}")
    public void receiveMessage(String message) throws IOException {
        VideoPathDTO videoPathDTO = objectMapper.readValue(message, VideoPathDTO.class);
        String filename = VideoUtils.SaveVideoToFile(videoPathDTO.getVideo().get("video1"));
        videoService.addUpdateRecord(videoPathDTO.getJobId(), filename, 100, VideoStatus.SAVED.toString());
    }

    @RabbitListener(queues = "${rabbitmq.queue.save.compress.video:savedVideo}")
    public void handleCompressedVideo(String message) throws IOException {
        log.info("Received compressed video details from the queue.");
        Map<String, Object> videoMessage = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
        String jobId = (String) videoMessage.get("jobId");
        String filename = (String) videoMessage.get("filename");
        String videoBase64 = (String) videoMessage.get("video");

        byte[] videoBytes = Base64.getDecoder().decode(videoBase64);

        Path directoryPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        Path outputPath = directoryPath.resolve(filename);

        Files.write(outputPath, videoBytes);
        log.info("Compressed video saved successfully at: {}", outputPath);
    }



}
