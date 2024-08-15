package hng_java_boilerplate.videoconversion.utils;

import hng_java_boilerplate.video.exceptions.JobCreationError;
import hng_java_boilerplate.videoconversion.conversionEnum.VideoOutput;

import java.util.List;

public class VideoConversionUtils {
    public static VideoOutput getMatchingFormat(String outputFormat, List<VideoOutput> audioFormatList) {
        return audioFormatList.stream()
                .filter(audioOutput -> outputFormat.equals(audioOutput.getStatus()))
                .findFirst()
                .orElseThrow(() -> new JobCreationError("output_format not supported"));
    }
}
