package hng_java_boilerplate.exception;

public record AuthErrorResponse (
        String status,
        String message,
        Integer status_code
) {
}
