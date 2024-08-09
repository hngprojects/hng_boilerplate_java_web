package hng_java_boilerplate.waitlist.controller;

import hng_java_boilerplate.email.EmailServices.EmailProducerService;
import hng_java_boilerplate.waitlist.dto.WaitlistRequestDto;
import hng_java_boilerplate.waitlist.dto.WaitlistResponseDto;
import hng_java_boilerplate.waitlist.entity.Waitlist;
import hng_java_boilerplate.waitlist.service.WaitlistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
    public ResponseEntity<WaitlistResponseDto> createWaitlist(@Valid @RequestBody WaitlistRequestDto waitlistRequestDTO){
        Waitlist waitlist = new Waitlist();
        waitlist.setFullName(waitlistRequestDTO.getFullName());
        waitlist.setEmail(waitlistRequestDTO.getEmail());
        waitlistService.saveWaitlist(waitlist);

        String to = waitlist.getEmail();
        String subject = "Confirmation Email";
        String text = "You are all signed up!";
        emailProducerService.sendEmailMessage(to, subject, text);

        return new ResponseEntity<>(new WaitlistResponseDto("You are all signed up!"), HttpStatus.CREATED);
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
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