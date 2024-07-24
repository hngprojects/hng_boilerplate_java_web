package hng_java_boilerplate.SMS.serviceImpl;

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
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import hng_java_boilerplate.util.ConstantMessages;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    private RabbitTemplate rabbitTemplate;
    private SMSRepository smsRepository;
    private ObjectMapper objectMapper;
    private UserServiceImpl userService;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate, SMSRepository smsRepository,ObjectMapper objectMapper,UserServiceImpl userService){
        this.rabbitTemplate=rabbitTemplate;
        this.smsRepository=smsRepository;
        this.objectMapper=objectMapper;
        this.userService=userService;
    }
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
        User user = userService.getLoggedInUser();

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
            sms.setSender_id(user.getId());
            sms.setCreated_at(Instant.now());
            smsRepository.save(sms);

            status = ConstantMessages.SUCCESS.getMessage();
            status_code = 200;
            response_message = ConstantMessages.SMS_SENT_SUCCESSFULLY.getMessage();
            return new ResponseEntity<>(new SmsResponseDto(status, status_code, response_message), HttpStatus.OK);
        } catch (AmqpException exception) {
            throw new BadTwilioCredentialsException(ConstantMessages.FAILED.getMessage());
        }
        catch (NullPointerException exception) {
            throw new BadTwilioCredentialsException(ConstantMessages.FAILED.getMessage());
        }
        catch (Exception exception) {
            throw new BadTwilioCredentialsException(ConstantMessages.FAILED.getMessage());
        }
    }
}