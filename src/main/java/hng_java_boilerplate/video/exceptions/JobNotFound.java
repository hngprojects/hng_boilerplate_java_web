package hng_java_boilerplate.video.exceptions;

public class JobNotFound extends RuntimeException{
    public JobNotFound() {
    }

    public JobNotFound(String message) {
        super(message);
    }

    public JobNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public JobNotFound(Throwable cause) {
        super(cause);
    }
}
