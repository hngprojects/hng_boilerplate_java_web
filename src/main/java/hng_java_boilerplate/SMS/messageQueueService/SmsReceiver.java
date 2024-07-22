package hng_java_boilerplate.SMS.messageQueueService;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import hng_java_boilerplate.SMS.SmsException.PhoneNumberOrMessageNotValidException;
import hng_java_boilerplate.SMS.dto.SmsResponseDto;
import hng_java_boilerplate.util.ConstantMessages;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class SmsReceiver {
    @Value("${TWILIO_ACCOUNT_SID}")
    String ACCOUNT_SID;
    @Value("${TWILIO_AUTH_TOKEN}")
    String AUTH_TOKEN;
    @Value("${TWILIO_OUTGOING_SMS_NUMBER}")
    String OUTGOING_SMS_NUMBER;

    @PostConstruct
    public void setup(){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void receiveMessage(String smsRequest) {
        String[] parts = smsRequest.split(":");
        String phone_number = parts[0];
        String text_message = parts[1];

        if(phone_number.matches("^\\+\\d{10,15}$")){
            throw new PhoneNumberOrMessageNotValidException(ConstantMessages.INVALID_PHONE_NUMBER_OR_CONTENT.getMessage());
        }

            Message.creator(
                    new PhoneNumber(phone_number),
                    new PhoneNumber(OUTGOING_SMS_NUMBER),
                            text_message)
                    .create();
    }
}