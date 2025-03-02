package hng_java_boilerplate.helpCenter.contactUs.controller;

import hng_java_boilerplate.exception.ErrorResponseDto;
import hng_java_boilerplate.exception.ValidationError;
import hng_java_boilerplate.helpCenter.contactUs.dto.request.ContactUsRequest;
import hng_java_boilerplate.helpCenter.contactUs.dto.response.ContactUsResponse;
import hng_java_boilerplate.helpCenter.contactUs.dto.response.CustomResponse;
import hng_java_boilerplate.helpCenter.contactUs.service.ContactUsService;
import hng_java_boilerplate.helpCenter.topic.controller.TopicController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contact-us")
@Tag(name = "Contact Us", description = "Controller for contact us")
public class ContactUsController {
    private final ContactUsService contactUsService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "send a contact us message success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomResponse.class))
            ),
            @ApiResponse(responseCode = "422", description = "Invalid or missing required request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationError.class))
            ),
    })
    public ResponseEntity<CustomResponse> receiveContactMessage(@RequestBody @Valid ContactUsRequest request) {
        return ResponseEntity.ok(contactUsService.processContactMessage(request));
    }


    @GetMapping
    @Operation(summary = "Retrieve all contacts")
    public ResponseEntity<List<ContactUsResponse>> getAllContacts() {
        return ResponseEntity.ok(contactUsService.getAllContacts());
    }
}
