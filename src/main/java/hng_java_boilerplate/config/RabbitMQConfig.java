package hng_java_boilerplate.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String UPLOAD_QUEUE = "video.watermark.queue";
    public static final String STATUS_QUEUE = "video.status.queue";

    @Value("${rabbitmq.queue.email}")
    private String emailQueueName;

    @Value("${rabbitmq.queue.concat}")
    private String videoConcat;

    @Value("${rabbitmq.queue.finishedConcat}")
    private String finishedConcatJob;


    @Bean
    public Queue emailQueue() {
        return new Queue(emailQueueName, true);
    }

    @Bean
    public Queue videoConcatQueue(){return new Queue(videoConcat, true);}

    @Bean
    public Queue finishedConcatQueue(){return new Queue(finishedConcatJob, true);}
    @Bean
    public Queue uploadQueue(){return new Queue(UPLOAD_QUEUE, true);}

    @Bean
    public Queue statusQueue(){return new Queue(STATUS_QUEUE, true);}
}

