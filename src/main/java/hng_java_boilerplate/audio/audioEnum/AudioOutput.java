package hng_java_boilerplate.audio.audioEnum;

public enum AudioOutput {

    MP3("audio/mp3"), WAV("audio/wav"), OPUS("audio/opus"), FLAC("audio/flac") ;

    private final String status;

    AudioOutput(String status) {
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
