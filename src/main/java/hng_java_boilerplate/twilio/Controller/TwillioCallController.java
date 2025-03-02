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
    public CallResponse makecall(@RequestBody CallRequest callRequest) {
        if (callRequest == null || callRequest.getToNumber() == null || callRequest.getFromNumber() == null) {
            throw new BadRequestException("Invalid request: Phone number is required");
        }

        CallResponse response = twilioCallService.makeCall(callRequest);

        if (response == null) {
            throw new NotFoundException("Twilio service error: Service not found/available");
        }
        return response;
    }
}