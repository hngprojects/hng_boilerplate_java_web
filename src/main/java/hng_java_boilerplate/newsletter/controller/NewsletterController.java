package hng_java_boilerplate.newsletter.controller;

import hng_java_boilerplate.newsletter.dto.SubscribeRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeResponse;
import hng_java_boilerplate.newsletter.dto.SubscribersResponse;
import hng_java_boilerplate.newsletter.service.NewsletterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/newsletter-subscription")
public class NewsletterController {
    private final NewsletterService newsletterService;

    @PostMapping
    public ResponseEntity<SubscribeResponse> subscribe(@RequestBody @Valid SubscribeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newsletterService.subscribeToNewsletter(request));
    }

    @GetMapping
    public ResponseEntity<SubscribersResponse> getSubscribers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        SubscribersResponse response = newsletterService.getSubscribersResponse(page, size);
        return ResponseEntity.ok(response);
    }
}
