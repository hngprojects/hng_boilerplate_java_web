package hng_java_boilerplate.video.videoEnums;

public enum VideoMessage {

        PROCESSING("Process have begun"),
        SUCCESS("Video processing was successful"),
        FAILED("process failed");
        private final String status;

        VideoMessage(String status) {
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
