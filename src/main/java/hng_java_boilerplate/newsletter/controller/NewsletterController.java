package hng_java_boilerplate.newsletter.controller;

import hng_java_boilerplate.newsletter.dto.DeleteRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeResponse;
import hng_java_boilerplate.newsletter.entity.Newsletter;
import hng_java_boilerplate.newsletter.service.NewsletterService;
import hng_java_boilerplate.user.dto.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/newsletter")
public class NewsletterController {
    private final NewsletterService newsletterService;

    @PostMapping
    public ResponseEntity<SubscribeResponse> subscribe(@RequestBody @Valid SubscribeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newsletterService.subscribeToNewsletter(request));
    }

    @GetMapping("/{userId}/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<Newsletter>> getNewslettersByUserId(@PathVariable String userId,@PathVariable int pageNumber,@PathVariable int pageSize) {
        return ResponseEntity.ok(newsletterService.findNewsletterByUserId(userId,pageNumber,pageSize));
    }

    @GetMapping("/{date}/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<Newsletter>> getNewsletterAfterDate(@PathVariable LocalDateTime date, @PathVariable int pageNumber,@PathVariable int pageSize) {
        return ResponseEntity.ok(newsletterService.findNewsletterByCreatedAtAfter(date,pageNumber,pageSize));
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteNewsletterById(@Valid @RequestBody DeleteRequest request) {
        String user_id = request.getUser_id();
        if(user_id.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            Response<?> response = newsletterService.deleteNewsletterByUserId(user_id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}
