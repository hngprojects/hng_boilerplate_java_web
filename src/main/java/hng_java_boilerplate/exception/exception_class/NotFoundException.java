package hng_java_boilerplate.exception.exception_class;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
