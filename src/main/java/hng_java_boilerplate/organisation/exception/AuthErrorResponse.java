package hng_java_boilerplate.organisation.exception;

public record AuthErrorResponse (
        String status,
        String message,
        Integer status_code
) {
}
