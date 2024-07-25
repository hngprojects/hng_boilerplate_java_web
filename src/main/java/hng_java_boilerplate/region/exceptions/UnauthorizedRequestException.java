package hng_java_boilerplate.region.exceptions;

public class UnauthorizedRequestException extends RuntimeException {
    public UnauthorizedRequestException(String message) {
        super(message);
    }
}