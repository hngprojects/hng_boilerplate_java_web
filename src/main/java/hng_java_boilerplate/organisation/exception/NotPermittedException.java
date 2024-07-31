package hng_java_boilerplate.organisation.exception;

public class NotPermittedException extends RuntimeException{
    public NotPermittedException(String msg) {
        super(msg);
    }
}
