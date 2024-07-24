package hng_java_boilerplate.SMS.messageQueueService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import hng_java_boilerplate.SMS.SmsException.BadTwilioCredentialsException;
import hng_java_boilerplate.SMS.SmsException.PhoneNumberOrMessageNotValidException;
import hng_java_boilerplate.SMS.dto.SmsRequestDto;
import hng_java_boilerplate.SMS.dto.SmsResponseDto;
import hng_java_boilerplate.SMS.entity.SMS;
import hng_java_boilerplate.SMS.repository.SMSRepository;
import hng_java_boilerplate.SMS.service.RabbitMQService;
import hng_java_boilerplate.util.ConstantMessages;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class RabbitMQProducer implements RabbitMQService {

    @Value("${rabbitmq.exchange.key}")
    private String rabbitmq_exchange_key;
    @Value("${rabbitmq.routing.key}")
    private String rabbitmq_routing_key;
    @Value("${twilio.account.sid}")
    String ACCOUNT_SID;
    @Value("${twilio.auth.token}")
    String AUTH_TOKEN;
    @Value("${twilio.outgoing.sms.number}")
    String OUTGOING_SMS_NUMBER;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SMSRepository smsRepository;
    @Autowired
    private ObjectMapper objectMapper;
    String status;
    int status_code;
    String response_message;

    @PostConstruct
    public void setup(){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @Override
    public ResponseEntity<?> sendSMS(SmsRequestDto smsRequestDto){
        String phone_number = smsRequestDto.getPhone_number();
        String message_content = smsRequestDto.getMessage();

        if(!phone_number.matches("^\\+\\d{10,15}$") || message_content.isEmpty()){
            throw new PhoneNumberOrMessageNotValidException(ConstantMessages.INVALID_PHONE_NUMBER_OR_CONTENT.getMessage());
        }
        try {
            Message message = Message.creator(new PhoneNumber(phone_number), new PhoneNumber(OUTGOING_SMS_NUMBER), message_content).create();
            String messageJson = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend(rabbitmq_exchange_key, rabbitmq_routing_key, messageJson);

            SMS sms = new SMS();
            sms.setDestination_phone_number(smsRequestDto.getPhone_number());
            sms.setMessage(smsRequestDto.getMessage());
            sms.setSender_id("75c164be-d14c-4c12-93bd-a2cd54a0c61c");
            sms.setCreated_at(Instant.now());
            smsRepository.save(sms);

            status = ConstantMessages.SUCCESS.getMessage();
            status_code = 200;
            response_message = ConstantMessages.SMS_SENT_SUCCESSFULLY.getMessage();
            return new ResponseEntity<>(new SmsResponseDto(status, status_code, response_message), HttpStatus.OK);
        } catch (Exception exception) {
            throw new BadTwilioCredentialsException(ConstantMessages.FAILED.getMessage());
        }
    }
}