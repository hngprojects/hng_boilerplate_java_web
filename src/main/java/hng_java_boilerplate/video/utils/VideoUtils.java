package hng_java_boilerplate.video.utils;

import hng_java_boilerplate.video.dto.DownloadableDTO;
import hng_java_boilerplate.video.dto.VideoCurrentStatusDTO;
import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.dto.VideoStatusDTO;
import hng_java_boilerplate.video.entity.VideoSuite;
import hng_java_boilerplate.video.exceptions.FileDoesNotExist;
import hng_java_boilerplate.video.exceptions.JobCreationError;
import hng_java_boilerplate.video.service.VideoService;
import hng_java_boilerplate.video.videoEnums.VideoOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


public class VideoUtils {


    public static String UPLOAD_DIR = "videoFiles";
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);

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


    public static byte[] applyWatermark(byte[] videoBytes, byte[] watermarkBytes, String position, int size, int transparency) {
        return videoBytes;
    }

    public static byte[] applyImageWatermark(byte[] videoBytes, byte[] watermarkImageBytes, String position, int size, int transparency) throws IOException {
        BufferedImage videoImage = byteArrayToBufferedImage(videoBytes);
        BufferedImage watermarkImage = byteArrayToBufferedImage(watermarkImageBytes);

        Graphics2D g2d = videoImage.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency / 100.0f));

        // Set watermark position
        int x = position.equals("bottom-right") ? videoImage.getWidth() - watermarkImage.getWidth() : 0;
        int y = position.equals("bottom-right") ? videoImage.getHeight() - watermarkImage.getHeight() : 0;

        g2d.drawImage(watermarkImage, x, y, null);
        g2d.dispose();

        return bufferedImageToByteArray(videoImage);
    }

    public static byte[] applyTextWatermark(byte[] videoBytes, String watermarkText, String position, int size, int transparency) throws IOException {
        BufferedImage videoImage = byteArrayToBufferedImage(videoBytes);

        Graphics2D g2d = videoImage.createGraphics();
        g2d.setFont(new Font("Arial", Font.BOLD, size));
        g2d.setColor(new Color(255, 255, 255, transparency)); // White color with transparency
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency / 100.0f));


        int x = position.equals("bottom-right") ? videoImage.getWidth() - g2d.getFontMetrics().stringWidth(watermarkText) - 10 : 10;
        int y = position.equals("bottom-right") ? videoImage.getHeight() - 10 : g2d.getFontMetrics().getAscent() + 10;

        g2d.drawString(watermarkText, x, y);
        g2d.dispose();

        return bufferedImageToByteArray(videoImage);
    }


    private static BufferedImage byteArrayToBufferedImage(byte[] bytes) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return ImageIO.read(bais);
    }


    private static byte[] bufferedImageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    public static VideoOutput getMatchingFormat(String outputFormat, List<VideoOutput> audioFormatList) {
        return audioFormatList.stream()
                .filter(audioOutput -> outputFormat.equals(audioOutput.getStatus()))
                .findFirst()
                .orElseThrow(() -> new JobCreationError("output_format not supported"));
    }


}
