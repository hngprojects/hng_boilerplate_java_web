package hng_java_boilerplate.exception.exception_class;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
