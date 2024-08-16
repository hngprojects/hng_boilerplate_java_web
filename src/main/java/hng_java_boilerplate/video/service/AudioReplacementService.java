package hng_java_boilerplate.video.service;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.springframework.stereotype.Service;

@Service
public class AudioReplacementService {

    public void replaceAudio(String videoFilePath, String audioFilePath, String outputFilePath) throws Exception {
        FFmpegFrameGrabber videoGrabber = new FFmpegFrameGrabber(videoFilePath);
        FFmpegFrameGrabber audioGrabber = new FFmpegFrameGrabber(audioFilePath);

        videoGrabber.start();
        audioGrabber.start();

        String outputFormat = getFileExtension(outputFilePath);

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFilePath,
                videoGrabber.getImageWidth(), videoGrabber.getImageHeight(), audioGrabber.getAudioChannels());

        recorder.setFormat(outputFormat);
        recorder.setFrameRate(videoGrabber.getFrameRate());
        recorder.setSampleRate(audioGrabber.getSampleRate());

        recorder.start();

        for (int i = 0; i < videoGrabber.getLengthInFrames(); i++) {
            recorder.record(videoGrabber.grabFrame());
        }

        for (int i = 0; i < audioGrabber.getLengthInFrames(); i++) {
            recorder.record(audioGrabber.grabSamples());
        }

        recorder.stop();
        videoGrabber.stop();
        audioGrabber.stop();
    }

        public void adjustAudioSync(String videoFilePath, String audioFilePath, String outputFilePath, double offsetSeconds) throws Exception {
            FFmpegFrameGrabber videoGrabber = new FFmpegFrameGrabber(videoFilePath);
            FFmpegFrameGrabber audioGrabber = new FFmpegFrameGrabber(audioFilePath);

            videoGrabber.start();
            audioGrabber.start();

            String outputFormat = getFileExtension(outputFilePath);

            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFilePath,
                    videoGrabber.getImageWidth(), videoGrabber.getImageHeight(), audioGrabber.getAudioChannels());

            recorder.setFormat(outputFormat);
            recorder.setFrameRate(videoGrabber.getFrameRate());
            recorder.setSampleRate(audioGrabber.getSampleRate());

            recorder.start();

            int audioFrameSkip = (int) (offsetSeconds * audioGrabber.getFrameRate());
            for (int i = 0; i < audioFrameSkip; i++) {
                audioGrabber.grabFrame();
            }

            // Copy video and adjusted audio frames
            for (int i = 0; i < videoGrabber.getLengthInFrames(); i++) {
                recorder.record(videoGrabber.grabFrame());
                recorder.record(audioGrabber.grabSamples());
            }

            recorder.stop();
            videoGrabber.stop();
            audioGrabber.stop();
        }

        private String getFileExtension(String filePath) {
            return filePath.substring(filePath.lastIndexOf('.') + 1);
        }
    }


