package hng_java_boilerplate.video.errorhandler;

import hng_java_boilerplate.video.dto.VideoMessageDTO;
import hng_java_boilerplate.video.exceptions.JobCreationError;
import hng_java_boilerplate.video.exceptions.JobNotFound;
import hng_java_boilerplate.video.videoEnums.VideoMessage;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@RequiredArgsConstructor
public class VideoErrorHandler {

    private final VideoMessageDTO videoMessageDTO;
    @ExceptionHandler
    public ResponseEntity<?> handleException(MaxUploadSizeExceededException e){
        videoMessageDTO.setMessage("Total video size should not be more than 50mb");
        videoMessageDTO.setSuccess(false);
        videoMessageDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(videoMessageDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(JobCreationError e){
        videoMessageDTO.setMessage(e.getMessage());
        videoMessageDTO.setSuccess(false);
        videoMessageDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(videoMessageDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(JobNotFound e){
        videoMessageDTO.setMessage(e.getMessage());
        videoMessageDTO.setSuccess(false);
        videoMessageDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(videoMessageDTO, HttpStatus.BAD_REQUEST);
    }
}
