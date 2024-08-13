package hng_java_boilerplate.video.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.video.dto.VideoPathDTO;
import hng_java_boilerplate.video.utils.VideoUtils;
import hng_java_boilerplate.video.videoEnums.VideoStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class VideoServiceConsumer {

    private final ObjectMapper objectMapper;
    private final VideoService videoService;

    @RabbitListener(queues = "${rabbitmq.queue.concat}")
    public void receiveMessage(String message) throws IOException {
        VideoPathDTO videoPathDTO = objectMapper.readValue(message, VideoPathDTO.class);
        String filename = VideoUtils.SaveMediaToFile(videoPathDTO.getVideo().get("video1"));
        videoService.addUpdateRecord(videoPathDTO.getJobId(), filename, 100, VideoStatus.SAVED.toString());
    }
}
