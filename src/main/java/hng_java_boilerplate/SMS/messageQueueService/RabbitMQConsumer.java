package hng_java_boilerplate.SMS.messageQueueService;

import hng_java_boilerplate.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    private RabbitMQConfig rabbitMQConfig;

    public RabbitMQConsumer(RabbitMQConfig rabbitMQConfig){
        this.rabbitMQConfig=rabbitMQConfig;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(String message){

    }
}
