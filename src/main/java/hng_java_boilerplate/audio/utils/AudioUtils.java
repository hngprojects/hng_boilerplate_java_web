package hng_java_boilerplate.audio.utils;

import hng_java_boilerplate.audio.audioEnum.AudioOutput;
import hng_java_boilerplate.video.exceptions.JobCreationError;

import java.util.List;

public class AudioUtils {

    public static AudioOutput getMatchingFormat(String outputFormat, List<AudioOutput> audioFormatList) {
        return audioFormatList.stream()
                .filter(audioOutput -> outputFormat.equals(audioOutput.getStatus()))
                .findFirst()
                .orElseThrow(() -> new JobCreationError("output_format not supported"));
    }
}
