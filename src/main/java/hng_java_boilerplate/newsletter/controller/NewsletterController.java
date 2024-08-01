package hng_java_boilerplate.newsletter.controller;

import hng_java_boilerplate.email.EmailServices.EmailProducerService;
import hng_java_boilerplate.email.entity.EmailMessage;
import hng_java_boilerplate.newsletter.dto.NewsletterEmailRequestDto;
import hng_java_boilerplate.newsletter.dto.NewsletterEmailResponseDto;
import hng_java_boilerplate.newsletter.dto.NewsletterSubscribersResponseDto;
import hng_java_boilerplate.newsletter.service.NewsletterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pages")
public class NewsletterController {

    private final NewsletterService newsletterService;

    private final EmailProducerService emailProducerService;

    @Autowired
    public NewsletterController(NewsletterService newsletterService, EmailProducerService emailProducerService){
        this.newsletterService = newsletterService;
        this.emailProducerService = emailProducerService;

    }

    @PostMapping("/newsletter")
    public ResponseEntity<?> joinNewsletter(@Valid @RequestBody NewsletterEmailRequestDto subscriptionEmailRequestDto) {

        NewsletterEmailResponseDto newsletterEmailResponse = newsletterService.joinNewsletter(subscriptionEmailRequestDto);

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setSendTo(subscriptionEmailRequestDto.getEmail());
        emailMessage.setSubject("Welcome New Subscriber");
        emailMessage.setText("Welcome on board. We are glad to have you");
        emailProducerService.sendEmailMessage(emailMessage.getSendTo(), emailMessage.getSubject(), emailMessage.getText());

        return ResponseEntity.status(HttpStatus.CREATED).body(newsletterEmailResponse);

    }

    @GetMapping("/listOfNewslettersSubscribers")
    public ResponseEntity<NewsletterSubscribersResponseDto> getListOfNewsletterSubscribers(){
        return ResponseEntity.status(HttpStatus.CREATED).body(newsletterService.getListOfSubscribers());

    }

}
