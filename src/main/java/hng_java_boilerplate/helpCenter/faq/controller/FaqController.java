package hng_java_boilerplate.helpCenter.faq.controller;

import hng_java_boilerplate.exception.ErrorResponseDto;
import hng_java_boilerplate.exception.ValidationError;
import hng_java_boilerplate.helpCenter.faq.dto.request.FaqRequest;
import hng_java_boilerplate.helpCenter.faq.dto.response.CustomResponse;
import hng_java_boilerplate.helpCenter.faq.dto.response.FaqResponse;
import hng_java_boilerplate.helpCenter.faq.service.FaqService;
import hng_java_boilerplate.helpCenter.topic.controller.TopicController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/faqs")
@Tag(name="Faq", description = "Controller for FAQ")
public class FaqController {
    private final FaqService faqService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get all FAQs success",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FaqResponse.class)))
            ),
    })
    @Operation(summary = "Gets all frequently asked questions")
    public ResponseEntity<List<FaqResponse>> getFaqs() {
        return ResponseEntity.ok(faqService.getFaqs());
    }


    @PostMapping
    @Secured("ROLE_ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "create FAQ success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FaqResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "user not admin",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "422", description = "Invalid or missing required request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationError.class))
            ),
    })
    public ResponseEntity<FaqResponse> createFaqs(@RequestBody @Valid FaqRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(faqService.createFaq(request));
    }


    @Secured("ROLE_ADMIN")
    @PatchMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "update a FAQ",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "user not admin",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "FAQ do not exist with id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicController.ErrorResponse.class))
            ),
    })
    @Operation(summary = "Admin user updates existing FAQ")
    public ResponseEntity<CustomResponse> updateFaq(@RequestBody FaqRequest request, @PathVariable String id) {
        return ResponseEntity.ok(faqService.updateFaq(request, id));
    }


    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "delete a FAQ by ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "user not admin",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "FAQ do not exist with id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TopicController.ErrorResponse.class))
            ),
    })
    @Operation(summary = "User with admin privilege delete a faq by it's ID")
    public ResponseEntity<CustomResponse> deleteFaq(@PathVariable String id) {
        return ResponseEntity.ok(faqService.deleteFaq(id));
    }
}
