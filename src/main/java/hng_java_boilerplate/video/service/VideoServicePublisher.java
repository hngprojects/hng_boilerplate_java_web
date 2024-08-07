package hng_java_boilerplate.video.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.video.dto.VideoPathDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoServicePublisher {

    private final RabbitTemplate rabbitTemplate;

//    @Value("${rabbitmq.queue.concat}")
    private String videoConcat;
    private static final Logger logger = LoggerFactory.getLogger(VideoServicePublisher.class);
    public boolean sendVideoConcat(VideoPathDTO videoPathDTO){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(videoPathDTO);
            rabbitTemplate.convertAndSend(videoConcat, jsonString);
            return true;
        }catch (AmqpException e){
            return false;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
