package hng_java_boilerplate.video.controller;

import hng_java_boilerplate.video.dto.VideoUploadDTO;
import hng_java_boilerplate.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        VideoUploadDTO videoUploadDTO = new VideoUploadDTO();
        videoUploadDTO.setVideos(files);

       return new ResponseEntity<>(videoService.videoConcat(videoUploadDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<?> getJob(@PathVariable("jobId") String jobId){
        return new ResponseEntity<>(videoService.getJob(jobId), HttpStatus.OK);
    }
}
