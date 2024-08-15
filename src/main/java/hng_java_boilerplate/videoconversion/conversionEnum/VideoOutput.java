package hng_java_boilerplate.videoconversion.conversionEnum;

public enum VideoOutput {
    MP4("video/mp4"), AVI("video/avi"), MOV("video/mov"), MKV("video/mkv") ;

    private final String status;

    VideoOutput(String status) {
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
