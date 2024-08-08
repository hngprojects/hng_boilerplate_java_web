package hng_java_boilerplate.video.exceptions;

public class FileDoesNotExist extends RuntimeException{

    public FileDoesNotExist() {
    }

    public FileDoesNotExist(String message) {
        super(message);
    }

    public FileDoesNotExist(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDoesNotExist(Throwable cause) {
        super(cause);
    }
}
