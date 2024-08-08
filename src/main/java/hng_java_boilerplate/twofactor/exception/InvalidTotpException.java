package hng_java_boilerplate.twofactor.exception;

public class InvalidTotpException extends RuntimeException{
    public InvalidTotpException(String message) {
        super(message);
    }
}
