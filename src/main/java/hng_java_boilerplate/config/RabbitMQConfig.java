package hng_java_boilerplate.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;


@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.email}")
    private String emailQueueName;

    @Value("${rabbitmq.queue.concat}")
    private String videoConcat;

    @Value("${rabbitmq.queue.finishedConcat}")
    private String finishedConcatJob;

    @Value("${rabbitmq.queue.compress}")
    private String videoCompress;

    @Value("${rabbitmq.queue.save.compress.video:savedVideo}")
    private String saveCompressedVideos;



    @Bean
    public Queue emailQueue() {
        return new Queue(emailQueueName, true);
    }

    @Bean
    public Queue videoConcatQueue(){return new Queue(videoConcat, true);}

    @Bean
    public Queue finishedConcatQueue(){return new Queue(finishedConcatJob, true);}

    @Bean
    public Queue videoCompressQueue(){
        return new Queue(videoCompress, true);
    }

    @Bean
    public Queue saveCompressedVideosQueue(){ return new Queue(saveCompressedVideos, true);}


}
