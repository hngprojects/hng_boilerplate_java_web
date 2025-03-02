package hng_java_boilerplate.newsletter.controller;

import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.exception.ValidationError;
import hng_java_boilerplate.newsletter.dto.DeleteNewsletterRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeResponse;
import hng_java_boilerplate.newsletter.dto.SubscribersResponse;
import hng_java_boilerplate.newsletter.entity.Newsletter;
import hng_java_boilerplate.newsletter.service.NewsletterService;
import hng_java_boilerplate.user.dto.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "NewsLetter", description = "controller for newsletter")
public class NewsletterController {
    private final NewsletterService newsletterService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "user newsletter subscription success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubscribeResponse.class))
            ),
            @ApiResponse(responseCode = "422", description = "Invalid or missing required request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationError.class))
            ),
    })
    @Operation(summary = "User subscribe to newsletter")
    public ResponseEntity<SubscribeResponse> subscribe(@RequestBody @Valid SubscribeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newsletterService.subscribeToNewsletter(request));
    }

    @GetMapping("/{email}")
    public ResponseEntity<Page<Newsletter>> getNewslettersByEmail(@PathVariable String email, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(newsletterService.findNewsletterByEmail(email,pageable));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<Page<Newsletter>> getNewslettersAfterDate(@PathVariable LocalDateTime date, @PageableDefault(sort = "createdAt",direction = Sort.Direction.DESC)Pageable pageable) {
        return ResponseEntity.ok(newsletterService.findNewsletterByCreatedAtAfter(date,pageable));
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> deleteNewslettersByEmail(@Valid @RequestBody DeleteNewsletterRequest request) {
        String user_id = request.getEmail();
        if(user_id.isEmpty()){
            throw new BadRequestException("user id is required");
        }else {
            Response<?> response = newsletterService.deleteNewsletterByUserId(user_id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }


    @GetMapping
    public ResponseEntity<SubscribersResponse> getSubscribers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        SubscribersResponse response = newsletterService.getSubscribersResponse(page, size);
        return ResponseEntity.ok(response);
    }
}
