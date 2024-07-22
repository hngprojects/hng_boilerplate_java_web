package hng_java_boilerplate.SMS.service;

import hng_java_boilerplate.SMS.dto.SmsRequestDto;
import org.springframework.http.ResponseEntity;

public interface RabbitMQService {
    ResponseEntity<?> sendSMS(SmsRequestDto smsRequestDto);
}
