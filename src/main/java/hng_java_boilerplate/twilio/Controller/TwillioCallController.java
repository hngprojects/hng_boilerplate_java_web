package hng_java_boilerplate.twilio.Controller;

import com.twilio.exception.TwilioException;
import hng_java_boilerplate.twilio.CallLogs.ErrorResponse;
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new hng_java_boilerplate.twilio.CallLogs.ErrorResponse("Invalid request", e.getMessage())); // 400 Bad Request
        } catch (TwilioException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new hng_java_boilerplate.twilio.CallLogs.ErrorResponse("Twilio service error", e.getMessage())); // 503 Service Unavailable
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal server error", e.getMessage())); // 500 Internal Server Error
        }
    }
}