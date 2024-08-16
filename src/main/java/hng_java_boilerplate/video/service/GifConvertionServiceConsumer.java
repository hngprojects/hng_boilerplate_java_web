package hng_java_boilerplate.video.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.video.dto.FinishedDTO;
import hng_java_boilerplate.video.utils.VideoUtils;
import hng_java_boilerplate.video.videoEnums.VideoStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class GifConvertionServiceConsumer {
    private final ObjectMapper objectMapper;
    private final VideoService videoService;

    @RabbitListener(queues = "${rabbitmq.queue.finishedGifConversion:finishedGifConversion}")
    public void receiveGifConversionMessage(String message) throws IOException {
        FinishedDTO finishedDTO = objectMapper.readValue(message, FinishedDTO.class);
        String filename = VideoUtils.SaveMediaToFile(finishedDTO.getMedia());
        videoService.addUpdateRecord(finishedDTO.getJobId(), filename, 100, VideoStatus.SAVED.toString());
    }
}
