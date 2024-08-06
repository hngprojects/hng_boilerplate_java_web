package hng_java_boilerplate.video.errorhandler;

import hng_java_boilerplate.video.dto.VideoMessageDTO;
import hng_java_boilerplate.video.exceptions.JobCreationError;
import hng_java_boilerplate.video.exceptions.JobNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class VideoErrorHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<VideoMessageDTO> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        VideoMessageDTO videoMessageDTO = new VideoMessageDTO(
                "Total video size should not be more than 50mb",
                HttpStatus.BAD_REQUEST.value(),
                false
        );
        return new ResponseEntity<>(videoMessageDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JobCreationError.class)
    public ResponseEntity<VideoMessageDTO> handleJobCreationError(JobCreationError e) {
        VideoMessageDTO videoMessageDTO = new VideoMessageDTO(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                false
        );
        return new ResponseEntity<>(videoMessageDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JobNotFound.class)
    public ResponseEntity<VideoMessageDTO> handleJobNotFound(JobNotFound e) {
        VideoMessageDTO videoMessageDTO = new VideoMessageDTO(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                false
        );
        return new ResponseEntity<>(videoMessageDTO, HttpStatus.NOT_FOUND);
    }
}
