package hng_java_boilerplate.organisation.exception;

public record ValidationError(
        String field,
        String message
) {
}
