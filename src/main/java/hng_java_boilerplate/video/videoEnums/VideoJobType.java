package hng_java_boilerplate.video.videoEnums;

public enum VideoJobType {

    MERGE_VIDEO("merge video"),
    COMPRESS_VIDEO("compress video");
    private final String status;

    VideoJobType(String status) {
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
