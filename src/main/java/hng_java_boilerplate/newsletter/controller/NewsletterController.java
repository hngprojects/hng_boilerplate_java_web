package hng_java_boilerplate.newsletter.controller;

import hng_java_boilerplate.exception.UnAuthorizedException;
import hng_java_boilerplate.newsletter.dto.SubscribeRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeResponse;
import hng_java_boilerplate.newsletter.dto.UnsubscribeResponse;
import hng_java_boilerplate.newsletter.service.NewsletterService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
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
    private final UserService userService;

    @PostMapping
    public ResponseEntity<SubscribeResponse> subscribe(@RequestBody @Valid SubscribeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newsletterService.subscribeToNewsletter(request));
    }
    // ✅ Unsubscribe from Newsletter
    @DeleteMapping("/{subscriberId}")
    public ResponseEntity<?> unsubscribe(
            @PathVariable("subscriberId") String subscriberId
    ) {
        User authenticateduser =userService.getLoggedInUser();
        if(authenticateduser==null){
            throw new UnAuthorizedException("unathorised user");
        }
        UnsubscribeResponse bun=newsletterService.unsubscribeFromNews(subscriberId);
        return ResponseEntity.status(HttpStatus.OK).body(bun);
    }
}
