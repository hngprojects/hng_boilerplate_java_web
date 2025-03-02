package hng_java_boilerplate.newsletter.controller;


import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.newsletter.dto.DeleteRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeResponse;
import hng_java_boilerplate.newsletter.entity.Newsletter;
import hng_java_boilerplate.newsletter.service.NewsletterService;
import hng_java_boilerplate.user.dto.response.Response;

import hng_java_boilerplate.categories.dto.CategoryDto;
import hng_java_boilerplate.exception.ErrorResponseDto;
import hng_java_boilerplate.exception.ValidationError;
import hng_java_boilerplate.newsletter.dto.SubscribeRequest;
import hng_java_boilerplate.newsletter.dto.SubscribeResponse;
import hng_java_boilerplate.newsletter.dto.SubscribersResponse;
import hng_java_boilerplate.newsletter.service.NewsletterService;
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
import org.springframework.http.ResponseEnti
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/newsletter")

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/newsletter-subscription")
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


    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Newsletter>> getNewslettersByUserId(@PathVariable String userId, @PageableDefault(sort = "user_id", direction = Sort.Direction.DESC)Pageable pageable) {
        return ResponseEntity.ok(newsletterService.findNewsletterByUserId(userId,pageable));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<Page<Newsletter>> getNewslettersAfterDate(@PathVariable LocalDateTime date, @PageableDefault(sort = "created_at",direction = Sort.Direction.DESC)Pageable pageable) {
        return ResponseEntity.ok(newsletterService.findNewsletterByCreatedAtAfter(date,pageable));
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteNewslettersById(@Valid @RequestBody DeleteRequest request) {
        String user_id = request.getUser_id();
        if(user_id.isEmpty()){
            throw new BadRequestException("user id is required");
        }else {
            Response<?> response = newsletterService.deleteNewsletterByUserId(user_id);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

    @GetMapping
    public ResponseEntity<SubscribersResponse> getSubscribers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        SubscribersResponse response = newsletterService.getSubscribersResponse(page, size);
        return ResponseEntity.ok(response);

    }
}
