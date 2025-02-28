package hng_java_boilerplate.twilio.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import hng_java_boilerplate.twilio.RequestAndResponse.CallRequest;
import hng_java_boilerplate.twilio.RequestAndResponse.CallResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class TwilioCallService {
    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.from.number}")
    private String fromNumber;

    public TwilioCallService(){
        Twilio.init(accountSid,authToken);
    }

    public CallResponse makeCall (CallRequest callRequest){
        Call call = Call.creator(
                new PhoneNumber(callRequest.getToNumber()),
                new PhoneNumber(fromNumber),
                URI.create(callRequest.getMessageUrl())
        ).create();

        return new CallResponse("Call Initiated Successfully", call.getSid());
    }

}
