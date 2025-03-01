package hng_java_boilerplate.newsletter.controller;

import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.newsletter.dto.DeleteRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeResponse;
import hng_java_boilerplate.newsletter.entity.Newsletter;
import hng_java_boilerplate.newsletter.service.NewsletterService;
import hng_java_boilerplate.user.dto.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/{userId}")
    public ResponseEntity<Page<Newsletter>> getNewslettersByUserId(@PathVariable String userId, @PageableDefault(sort = "user_id", direction = Sort.Direction.DESC)Pageable pageable) {
        return ResponseEntity.ok(newsletterService.findNewsletterByUserId(userId,pageable));
    }

    @GetMapping("/{date}")
    public ResponseEntity<Page<Newsletter>> getNewslettersAfterDate(@PathVariable LocalDateTime date, @PageableDefault(sort = "created_at",direction = Sort.Direction.DESC)Pageable pageable) {
        return ResponseEntity.ok(newsletterService.findNewsletterByCreatedAtAfter(date,pageable));
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteNewslettersById(@Valid @RequestBody DeleteRequest request) {
        String user_id = request.getUser_id();
        if(user_id.isEmpty()){
            throw new BadRequestException("user id is required");
        }else {
            Response<?> response = newsletterService.deleteNewsletterByUserId(user_id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}
