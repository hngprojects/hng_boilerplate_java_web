package hng_java_boilerplate.twilio.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import hng_java_boilerplate.twilio.CallLogs.Entity;
import hng_java_boilerplate.twilio.Repository.TwilioCallRepo;
import hng_java_boilerplate.twilio.RequestAndResponse.CallRequest;
import hng_java_boilerplate.twilio.RequestAndResponse.CallResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TwilioCallService {

    @Value("${spring.twilio.account.sid}")
    private String accountSid;

    @Value("${spring.twilio.auth.token}")
    private String authToken;

    @Value("${spring.twilio.from.number}")
    private String fromNumber;

    private final TwilioCallRepo twilioCallRepo;

    @PostConstruct
    public void init(){
        Twilio.init(accountSid,authToken);
    }

    public CallResponse makeCall (CallRequest callRequest){

        Call call = Call.creator(
                new PhoneNumber(callRequest.getToNumber()),
                new PhoneNumber(callRequest.getFromNumber()),
                URI.create("http://demo.twilio.com/docs/voice.xml")
        ).create();


        Entity entity = Entity.builder()
                .toNumber(callRequest.getToNumber())
                .fromNumber(callRequest.getFromNumber())
                .callSid(call.getSid())
                .timestamp(LocalDateTime.now())
                .build();

        twilioCallRepo.save(entity);

        return new CallResponse("Call Initiated Successfully", call.getSid());
    }

}
