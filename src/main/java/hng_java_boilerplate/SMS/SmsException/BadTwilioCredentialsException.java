package hng_java_boilerplate.SMS.SmsException;

public class BadTwilioCredentialsException extends RuntimeException{
    public BadTwilioCredentialsException(String message){
        super(message);
    }
}
