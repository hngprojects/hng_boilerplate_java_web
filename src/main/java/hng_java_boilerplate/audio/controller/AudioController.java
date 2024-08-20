package hng_java_boilerplate.audio.controller;

import hng_java_boilerplate.audio.audioEnum.AudioOutput;
import hng_java_boilerplate.audio.service.AudioService;
import hng_java_boilerplate.audio.utils.AudioUtils;
import hng_java_boilerplate.video.dto.DownloadDTO;
import hng_java_boilerplate.video.dto.DownloadableDTO;
import hng_java_boilerplate.video.dto.VideoCurrentStatusDTO;
import hng_java_boilerplate.video.dto.VideoResponseDTO;
import hng_java_boilerplate.video.entity.VideoSuite;
import hng_java_boilerplate.video.service.VideoService;
import hng_java_boilerplate.video.utils.VideoUtils;
import hng_java_boilerplate.video.videoEnums.VideoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/audio")
@RequiredArgsConstructor
public class AudioController {

    private final AudioService audioService;
    private final VideoService videoService;

    @PostMapping("/extractions")
    public ResponseEntity<?> extract(@RequestParam("video")MultipartFile video,
                                     @RequestParam("output_format")
                                     @NotNull(message = "output_format can't be null")
                                     @NotBlank(message = "output_format can't be blank")
                                     @NotEmpty(message = "output_format can't be empty")
                                     String outputFormat) throws IOException {

        List<AudioOutput> audioFormatList = Arrays.stream(AudioOutput.values())
                .toList();
        String format = AudioUtils.getMatchingFormat("audio/"+outputFormat, audioFormatList).toString();

        return new ResponseEntity<>(audioService.startAudioProcess(video,format), HttpStatus.CREATED);
    }

    @GetMapping("/{jobId}/status")
    public ResponseEntity<?> getJob(@PathVariable("jobId") String jobId){
        VideoSuite job = videoService.getJob(jobId);
        if(job.getCurrentProcess().equals(VideoStatus.SAVED.toString())) {
            VideoResponseDTO<DownloadDTO> responseDTO = new VideoResponseDTO<>();
            responseDTO.setMessage("Video is ready for download");
            responseDTO.setSuccess(true);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            responseDTO.setData(new DownloadDTO(job.getJobId(), "/api/v1/audio/"+jobId+"/download"));
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
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=audio."+contentType.substring(6))
                .contentType(MediaType.valueOf(contentType))
                .contentLength(downloadDTO.getVideoByteLength())
                .body(downloadDTO.getResource());
    }
}
