package hng_java_boilerplate.exception.exception_class;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }
}
