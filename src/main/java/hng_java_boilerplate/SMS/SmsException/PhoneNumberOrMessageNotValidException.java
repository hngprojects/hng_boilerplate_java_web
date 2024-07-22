package hng_java_boilerplate.SMS.SmsException;

public class PhoneNumberOrMessageNotValidException extends RuntimeException{
    public PhoneNumberOrMessageNotValidException(String message){
        super(message);
    }
}
