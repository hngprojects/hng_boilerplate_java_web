package hng_java_boilerplate.SMS.controller;

import hng_java_boilerplate.SMS.dto.SmsRequestDto;
import hng_java_boilerplate.SMS.service.RabbitMQService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/sms")
public class SmsController {
    @Autowired
    private RabbitMQService rabbitMQService;

    @PostMapping("/send")
    public ResponseEntity<?> sendSMS(@Valid @RequestBody SmsRequestDto smsRequestDto){
        return rabbitMQService.sendSMS(smsRequestDto);
    }
}
