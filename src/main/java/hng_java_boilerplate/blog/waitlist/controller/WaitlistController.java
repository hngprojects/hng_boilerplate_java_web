package hng_java_boilerplate.blog.waitlist.controller;

import hng_java_boilerplate.messaging.email.EmailServices.EmailProducerService;
import hng_java_boilerplate.blog.waitlist.entity.Waitlist;
import hng_java_boilerplate.blog.waitlist.service.WaitlistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    @Secured("ROLE_SUPER_ADMIN")
    public ResponseEntity<?> getWaitlistUsers(Pageable pageable) {
        Page<Waitlist> waitlistPage = waitlistService.getWaitlistUsers(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("users", waitlistPage.getContent());
        response.put("currentPage", waitlistPage.getNumber());
        response.put("totalItems", waitlistPage.getTotalElements());
        response.put("totalPages", waitlistPage.getTotalPages());
        response.put("status_code", HttpStatus.OK.value());
        response.put("message", "Retrieved waitlist users successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}