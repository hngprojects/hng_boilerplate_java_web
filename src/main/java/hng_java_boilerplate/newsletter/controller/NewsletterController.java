package hng_java_boilerplate.newsletter.controller;

import hng_java_boilerplate.newsletter.dto.DeleteRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeResponse;
import hng_java_boilerplate.newsletter.service.NewsletterService;
import hng_java_boilerplate.user.dto.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @GetMapping("/{userId}")
    public ResponseEntity<?> getNewslettersByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(newsletterService.findNewsletterByUserId(userId));
    }

    @GetMapping("/{date}")
    public ResponseEntity<?> getNewsletterAfterDate(@PathVariable LocalDateTime date) {
        return ResponseEntity.ok(newsletterService.findNewsletterByCreatedAtAfter(date));
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteNewsletterById(@Valid @RequestBody DeleteRequest request) {
        String user_id = request.getUser_id();
        Response<?> response = newsletterService.deleteNewsletterByUserId(user_id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
