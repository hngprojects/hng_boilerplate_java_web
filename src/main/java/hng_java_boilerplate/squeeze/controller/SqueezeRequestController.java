package hng_java_boilerplate.squeeze.controller;

import hng_java_boilerplate.email.EmailServices.EmailProducerService;

import hng_java_boilerplate.exception.ErrorResponseDto;
import hng_java_boilerplate.exception.ValidationError;
import hng_java_boilerplate.squeeze.entity.SqueezeRequest;
import hng_java_boilerplate.squeeze.service.SqueezeRequestService;
import hng_java_boilerplate.squeeze.dto.ResponseMessageDto;
import hng_java_boilerplate.user.dto.request.GetUserDto;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/squeeze")
@Validated
@RequiredArgsConstructor
@Tag(name="Squeeze", description = "Handles user request for squeeze page")
public class SqueezeRequestController {

    private final SqueezeRequestService service;
    private final EmailProducerService emailProducerService;

    @PostMapping
    @Operation(summary = "sign up to squeeze page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "sign up for squeeze page",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessageDto.class))
            ),
            @ApiResponse(responseCode = "409", description = "User already subscribed with email",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "422", description = "invalid or missing required request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationError.class))
            ),
    })
    public ResponseEntity<?> handleSqueezeRequest(@Valid @RequestBody SqueezeRequest request) {
        service.saveSqueezeRequest(request);

        String to = request.getEmail();
        String subject = "Email Template Confirmation";
        String text = "This is your email template";
        emailProducerService.sendEmailMessage(to, subject, text);

        return ResponseEntity.ok().body(new ResponseMessageDto("You are all signed up!", HttpStatus.OK.value()));
    }


    @Operation(summary = "update squeeze page request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "update squeeze page success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessageDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessageDto.class))
            ),
            @ApiResponse(responseCode = "422", description = "invalid or missing required request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationError.class))
            ),
    })
    @PutMapping
    public ResponseEntity<?> updateSqueezeRequest(@Valid @RequestBody SqueezeRequest request) {
        try {
            service.updateSqueezeRequest(request);
            return ResponseEntity.ok().body(new ResponseMessageDto("Your record has been successfully updated. You cannot update it again.", HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessageDto(e.getMessage(), HttpStatus.NOT_FOUND.value()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessageDto(e.getMessage(), HttpStatus.FORBIDDEN.value()));
        }
    }


    @Operation(summary = "get squeeze page requests")
    @ApiResponse(responseCode = "200", description = "retrieve squeeze page requests success",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SqueezeRequest.class)))
    )
    @GetMapping
    public ResponseEntity<?> getAllSqueezeRequests() {
        List<SqueezeRequest> squeezeRequests = service.getAllSqueezeRequests();
        return ResponseEntity.ok().body(squeezeRequests);
    }

}