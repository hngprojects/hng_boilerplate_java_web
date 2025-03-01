package hng_java_boilerplate.twilio.Controller;

import com.twilio.exception.TwilioException;
import hng_java_boilerplate.comment.dto.ErrorResponse;
import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.exception.CustomError;
import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.twilio.RequestAndResponse.CallRequest;
import hng_java_boilerplate.twilio.RequestAndResponse.CallResponse;
import hng_java_boilerplate.twilio.Service.TwilioCallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TwillioCallController {

    private final TwilioCallService twilioCallService;

    @PostMapping("/call")
    public ResponseEntity<?> makecall(@RequestBody CallRequest callRequest) {
        try {
            CallResponse response = twilioCallService.makeCall(callRequest);
            return ResponseEntity.ok(response); // 200 OK
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new hng_java_boilerplate.comment.dto.ErrorResponse("Invalid request","Bad request",400)); // 400 Bad Request
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new hng_java_boilerplate.comment.dto.ErrorResponse("Twilio service error","Service not found/available", 503)); // 503 Service Unavailable
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error", "server error",500)); // 500 Internal Server Error
        }
    }
}