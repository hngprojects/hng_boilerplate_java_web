package hng_java_boilerplate.video.exceptions;

public class JobCreationError extends RuntimeException{
    public JobCreationError() {
    }

    public JobCreationError(String message) {
        super(message);
    }

    public JobCreationError(String message, Throwable cause) {
        super(message, cause);
    }

    public JobCreationError(Throwable cause) {
        super(cause);
    }
}
