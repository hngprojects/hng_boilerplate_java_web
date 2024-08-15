package hng_java_boilerplate.plans.exceptions;

public class DuplicatePlanException extends RuntimeException {
    public DuplicatePlanException(String message) {
        super(message);
    }
}
