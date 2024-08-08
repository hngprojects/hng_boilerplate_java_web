package hng_java_boilerplate.video.exceptions;

public class VideoLengthConstaint extends RuntimeException{
    public VideoLengthConstaint() {
    }

    public VideoLengthConstaint(String message) {
        super(message);
    }

    public VideoLengthConstaint(String message, Throwable cause) {
        super(message, cause);
    }

    public VideoLengthConstaint(Throwable cause) {
        super(cause);
    }
}
