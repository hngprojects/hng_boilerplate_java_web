package hng_java_boilerplate.video.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.video.dto.FinishedDTO;
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

    @RabbitListener(queues = "${rabbitmq.queue.finishedConcat:finishedConcat}")
    public void receiveMessage(String message) throws IOException {
        FinishedDTO finishedDTO = objectMapper.readValue(message, FinishedDTO.class);
        String filename = VideoUtils.SaveMediaToFile(finishedDTO.getMedia());
        videoService.addUpdateRecord(finishedDTO.getJobId(), filename, 100, VideoStatus.SAVED.toString());
    }




}
