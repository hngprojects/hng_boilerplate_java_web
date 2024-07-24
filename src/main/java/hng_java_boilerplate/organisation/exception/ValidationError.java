package hng_java_boilerplate.exception;

public record ValidationError(
        String field,
        String message
) {
}
