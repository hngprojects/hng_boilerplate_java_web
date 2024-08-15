package hng_java_boilerplate.video.controller;

import hng_java_boilerplate.video.dto.*;
import hng_java_boilerplate.video.entity.VideoSuite;
import hng_java_boilerplate.video.exceptions.VideoLengthConstaint;
import hng_java_boilerplate.video.service.VideoService;
import hng_java_boilerplate.video.utils.VideoUtils;
import hng_java_boilerplate.video.videoEnums.VideoStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/merge")
    public ResponseEntity<?> concatVideo(@RequestParam("videos") List<MultipartFile> files) throws IOException {
        if(files.size() > 3)
            throw new VideoLengthConstaint("Not more than 3 videos");
        VideoUploadDTO videoUploadDTO = new VideoUploadDTO();
        videoUploadDTO.setVideos(files);

       return new ResponseEntity<>(videoService.videoConcat(videoUploadDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{jobId}/status")
    public ResponseEntity<?> getJob(@PathVariable("jobId") String jobId){
        VideoSuite job = videoService.getJob(jobId);
        if(job.getCurrentProcess().equals(VideoStatus.SAVED.toString())){
            VideoResponseDTO<DownloadDTO> responseDTO = new VideoResponseDTO<>();
            responseDTO.setMessage("Video is ready for download");
            responseDTO.setSuccess(true);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setData(new DownloadDTO(job.getJobId(), "/api/v1/videos/"+jobId+"/download"));
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }

        VideoResponseDTO<VideoCurrentStatusDTO> videoResponseDTO = VideoUtils.currentStatus("Found",
                HttpStatus.OK.value(), true, new VideoCurrentStatusDTO(job.getJobId(),
                        job.getCurrentProcess(), job.getProgress()));
        return new ResponseEntity<>(videoResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/{jobId}/download")
    public ResponseEntity<?> downloadJob(@PathVariable("jobId") String jobId) throws IOException {
        DownloadableDTO downloadDTO = videoService.downloadVideo(jobId);
        String contentType = downloadDTO.getContentType();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=video."
                        + contentType.substring(6))
                .contentType(MediaType.valueOf(downloadDTO.getContentType()))
                .contentLength(downloadDTO.getVideoByteLength())
                .body(downloadDTO.getResource());
    }

    @PostMapping("/compress")
    public ResponseEntity<?> compressVideo(
            @RequestParam("video") MultipartFile videoFile,
            @RequestParam(value = "resolution", required = false) String resolution,
            @RequestParam(value = "bitrate", required = false) String bitrate,
            @RequestParam(value = "outputFormat", required = false) String outputFormat)  {

        VideoCompressResponse<?> response;
        try {
            response = videoService.compressVideo(new  VideoCompressRequest(videoFile, resolution, bitrate, outputFormat));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{jobId}/compressed/download")
    public ResponseEntity<?> downloadCompressVide(@PathVariable("jobId") String jobId) throws IOException {
        DownloadableDTO downloadDTO = videoService.downloadCompressVideo(jobId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=video.mp4")
                .contentType(MediaType.valueOf("video/mp4"))
                .contentLength(downloadDTO.getVideoByteLength())
                .body(downloadDTO.getResource());
    }
}
