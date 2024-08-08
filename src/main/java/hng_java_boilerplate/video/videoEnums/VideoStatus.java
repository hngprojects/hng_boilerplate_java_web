package hng_java_boilerplate.video.videoEnums;

public enum VideoStatus {
    PENDING("Pending"),
    SUCCESS("Success"),
    PROCESSING("Processing"),
    FAILED("Failed"),
    SAVED("Saved");
    private final String status;

    VideoStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }

}
