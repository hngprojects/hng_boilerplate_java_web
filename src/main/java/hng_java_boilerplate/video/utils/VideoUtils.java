package hng_java_boilerplate.video.utils;

import hng_java_boilerplate.video.dto.*;
import hng_java_boilerplate.video.entity.VideoSuite;
import hng_java_boilerplate.video.exceptions.FileDoesNotExist;
import hng_java_boilerplate.video.exceptions.JobCreationError;
import hng_java_boilerplate.video.repository.VideoRepository;
import hng_java_boilerplate.video.service.VideoService;
import hng_java_boilerplate.video.videoEnums.JobType;
import hng_java_boilerplate.video.videoEnums.VideoMessage;
import hng_java_boilerplate.video.videoEnums.VideoOutput;
import hng_java_boilerplate.video.videoEnums.VideoStatus;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Component
public class VideoUtils {


    public static String UPLOAD_DIR = "videoFiles";
    public static String PENDING = "Pending";
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);

    private final VideoRepository videoRepository;

    public static String saveVideoTemp(MultipartFile file) {

        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
            }

            Path filePath = Paths.get(UPLOAD_DIR, file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            return filePath.toAbsolutePath().toString();

        } catch (IOException e) {
            logger.error("Error saving file: {}", e.getMessage(), e);
            return "File upload failed: " + e.getMessage();
        }
    }

    public static String generateUuid(){
        return UUID.randomUUID().toString();
    }

    public static VideoSuite videoSuite(String jobId, String status,
                                        String fileName, String jobType,
                                        String message, String currentProcess,
                                        String mediaFormat, String expectedFormat){
        VideoSuite videoSuite = new VideoSuite();

        videoSuite.setJobId(jobId);
        videoSuite.setStatus(status);
        videoSuite.setFilename(fileName);
        videoSuite.setJobType(jobType);
        videoSuite.setMessage(message);
        videoSuite.setCurrentProcess(currentProcess);
        videoSuite.setMediaFormat(mediaFormat);
        videoSuite.setExpectedFormat(expectedFormat);
        return videoSuite;
    }

    public static byte[] videoToByte(MultipartFile video) throws IOException {
        return video.getBytes();
    }

    public static VideoResponseDTO<VideoStatusDTO> response (String message, int statusCode,
                                                boolean success, VideoStatusDTO statusDTO){

        VideoResponseDTO<VideoStatusDTO> responseDTO = new VideoResponseDTO<>();
        responseDTO.setMessage(message);
        responseDTO.setSuccess(success);
        responseDTO.setStatusCode(statusCode);
        responseDTO.setData(statusDTO);
        return responseDTO;
    }

    public static VideoResponseDTO<VideoCurrentStatusDTO>currentStatus(String message, int statusCode,
                                                                   boolean success, VideoCurrentStatusDTO statusDTO){

        VideoResponseDTO<VideoCurrentStatusDTO> responseDTO = new VideoResponseDTO<>();
        responseDTO.setMessage(message);
        responseDTO.setSuccess(success);
        responseDTO.setStatusCode(statusCode);
        responseDTO.setData(statusDTO);
        return responseDTO;
    }

    public static String SaveMediaToFile(byte[] videoByte)throws IOException{
        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String filename = generateFileName();
        File videoFile = new File(UPLOAD_DIR + File.separator + filename);

        try (FileOutputStream fos = new FileOutputStream(videoFile)) {
            fos.write(videoByte);
        }
        return filename;
    }

    public static DownloadableDTO byteArrayResource(String filename) throws IOException {
        File file = new File(UPLOAD_DIR + File.separator + sanitizeFileName(filename));
        DownloadableDTO downloadDTO =new DownloadableDTO();

        if (!file.exists()) {
            throw new FileDoesNotExist("File not found");
        }

        byte[] videoByte = Files.readAllBytes(file.toPath());
        downloadDTO.setVideoByteLength(videoByte.length);
        downloadDTO.setResource(new ByteArrayResource(videoByte));
        return downloadDTO;
    }

    private static String generateFileName() {
        return "media_" + VideoUtils.generateUuid();
    }

    public static String sanitizeFileName(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    public static VideoOutput getMatchingFormat(String outputFormat, List<VideoOutput> audioFormatList) {
        return audioFormatList.stream()
                .filter(audioOutput -> outputFormat.equals(audioOutput.getStatus()))
                .findFirst()
                .orElseThrow(() -> new JobCreationError("output_format not supported"));
    }

    public VideoCompressResponse<?> buildVideoCompressResponse(VideoCompressRequest request, String jobId, String originalFileSize, String fileExtension) {
        VideoSuite videoSuite;
        videoSuite = VideoUtils.videoSuite(jobId, VideoStatus.PENDING.toString(), request.getVideoFile().getOriginalFilename(), JobType.COMPRESS_VIDEO.toString(), String.valueOf(VideoMessage.PENDING), VideoStatus.PENDING.toString(), fileExtension, "video/"+request.getOutputFormat());
        videoSuite.setBitrate(request.getBitrate());
        videoSuite.setResolution(request.getResolution());
        videoSuite.setOriginalFileSize(originalFileSize);
        videoRepository.save(videoSuite);
        VideoStatusDTO response = new VideoStatusDTO(jobId, PENDING, VideoMessage.PENDING.getStatus(), request.getVideoFile().getOriginalFilename(), "compress video", 0, PENDING, fileExtension, request.getOutputFormat());
        return VideoCompressResponse.builder().message("Job Created").statusCode(HttpStatus.CREATED.value()).data(response).build();
    }

    public void validateCompressionRequest(VideoCompressRequest request) {
        MultipartFile videoFile = request.getVideoFile();
        if (videoFile == null || videoFile.isEmpty()) {
            throw new IllegalArgumentException("Video file is required and cannot be empty.");
        }

        String filename = videoFile.getOriginalFilename();
        if (filename == null || !isValidVideoFormat(filename)) {
            throw new IllegalArgumentException("Invalid video format. Accepted formats are: .mp4, .avi");
        }

        List<String> validOutputFormats = Arrays.asList("mp4", "avi");
        if (!validOutputFormats.contains(request.getOutputFormat())) {
            throw new IllegalArgumentException("Invalid output format. Valid options are mp4, avi.");
        }
    }

    public static boolean isValidVideoFormat(String filename) {
        List<String> validFormats = Arrays.asList(".mp4", ".avi", ".mkv", ".mov");
        String fileExtension = filename.substring(filename.lastIndexOf('.')).toLowerCase();
        return validFormats.contains(fileExtension);
    }


    public Map<String, String> getFileSize(String jobId, VideoService videoService) {
        return videoService.getFileSize(jobId);
    }

    public static String formatFileSize(long sizeInBytes) {
        if (sizeInBytes < 1024) return sizeInBytes + " B";
        int exp = (int) (Math.log(sizeInBytes) / Math.log(1024));
        String pre = ("KMGTPE").charAt(exp - 1) + "B";
        return String.format("%.1f %s", sizeInBytes / Math.pow(1024, exp), pre);
    }


}
