package hng_java_boilerplate.waitlist.controller;

import hng_java_boilerplate.email.EmailServices.EmailProducerService;
import hng_java_boilerplate.waitlist.entity.Waitlist;
import hng_java_boilerplate.waitlist.service.WaitlistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/waitlist")
@Tag(name="Waitlist")
public class WaitlistController {

    private final WaitlistService waitlistService;
    private final EmailProducerService emailProducerService;

    public WaitlistController(WaitlistService waitlistService, EmailProducerService emailProducerService) {
        this.waitlistService = waitlistService;
        this.emailProducerService = emailProducerService;
    }

    @PostMapping
    public ResponseEntity<?> createWaitlist(@Valid @RequestBody Waitlist waitlist){
        waitlistService.saveWaitlist(waitlist);

        String to = waitlist.getEmail();
        String subject = "Confirmation Email";
        String text = "You are all signed up!";
        emailProducerService.sendEmailMessage(to, subject, text);

        Map<String, String> response = new HashMap<>();
        response.put("message", "You are all signed up!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
