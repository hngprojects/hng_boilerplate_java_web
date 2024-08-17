package hng_java_boilerplate.video.videoEnums;

public enum JobType {

    MERGE_VIDEO("merge video"),
    EXTRACT_AUDIO("extract audio"),
    CONVERT_VIDEO("encode video");
    private final String status;

    JobType(String status) {
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
