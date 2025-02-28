package hng_java_boilerplate.twilio.Controller;

import hng_java_boilerplate.twilio.RequestAndResponse.CallRequest;
import hng_java_boilerplate.twilio.RequestAndResponse.CallResponse;
import hng_java_boilerplate.twilio.Service.TwilioCallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/call")
@RequiredArgsConstructor
public class TwillioCallController {

    private final TwilioCallService twilioCallService;

    @PostMapping("/make")
    public ResponseEntity<CallResponse> makecall(@RequestBody CallRequest callRequest){
        CallResponse response = twilioCallService.makeCall(callRequest);
        return ResponseEntity.ok(response);
    }
}
