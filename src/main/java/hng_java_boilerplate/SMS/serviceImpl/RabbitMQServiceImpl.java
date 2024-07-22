package hng_java_boilerplate.SMS.serviceImpl;

import hng_java_boilerplate.SMS.dto.SmsRequestDto;
import hng_java_boilerplate.SMS.dto.SmsResponseDto;
import hng_java_boilerplate.SMS.entity.SMS;
import hng_java_boilerplate.SMS.repository.SMSRepository;
import hng_java_boilerplate.SMS.service.RabbitMQService;
import hng_java_boilerplate.config.RabbitMQConfig;
import hng_java_boilerplate.util.ConstantMessages;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RabbitMQServiceImpl implements RabbitMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SMSRepository smsRepository;
    String status;
    int status_code;
    String message;


    @Override
    public ResponseEntity<?> sendSMS(SmsRequestDto smsRequestDto) {
        if (smsRequestDto.getPhone_number() == null || smsRequestDto.getPhone_number().isEmpty() ||
                smsRequestDto.getMessage() == null || smsRequestDto.getMessage().isEmpty()) {

            status = ConstantMessages.UNSUCCESSFUL.getMessage();
            status_code = 400;
            message = ConstantMessages.INVALID_PHONE_NUMBER_OR_CONTENT.getMessage();
            return new ResponseEntity<>(new SmsResponseDto(status, status_code, message), HttpStatus.BAD_REQUEST);
        }
        try{
        String smsRequest = smsRequestDto.getPhone_number() + ":" + smsRequestDto.getMessage();
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, smsRequest);

        SMS sms = new SMS();
        sms.setDestination_phone_number(smsRequestDto.getPhone_number());
        sms.setMessage(smsRequestDto.getMessage());
        sms.setSender_id("10bcf403-3452-4286-9b31-e861bf83f10c");
        sms.setCreated_at(Instant.now());
        smsRepository.save(sms);

        status = ConstantMessages.SUCCESS.getMessage();
        status_code = 200;
        message = ConstantMessages.SMS_SENT_SUCCESSFULLY.getMessage();
        return new ResponseEntity<>(new SmsResponseDto(status, status_code, message), HttpStatus.OK);
        }
        catch (Exception exception) {
            String status = ConstantMessages.UNSUCCESSFUL.getMessage();
            int status_code = 500;
            String message = ConstantMessages.FAILED.getMessage();
            return new ResponseEntity<>(new SmsResponseDto(status, status_code, message), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
