package hng_java_boilerplate.video.errorhandler;

import hng_java_boilerplate.video.dto.VideoMessageDTO;
import hng_java_boilerplate.video.exceptions.FileDoesNotExist;
import hng_java_boilerplate.video.exceptions.JobCreationError;
import hng_java_boilerplate.video.exceptions.JobNotFound;
import hng_java_boilerplate.video.exceptions.VideoLengthConstaint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.FileNotFoundException;

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

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<VideoMessageDTO> handleFileNotFoundException (FileNotFoundException e){
        VideoMessageDTO videoMessageDTO = new VideoMessageDTO(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                false
        );
        return new ResponseEntity<>(videoMessageDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileDoesNotExist.class)
    public ResponseEntity<VideoMessageDTO> handleFileNotFoundException (FileDoesNotExist e){
        VideoMessageDTO videoMessageDTO = new VideoMessageDTO(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                false
        );
        return new ResponseEntity<>(videoMessageDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VideoLengthConstaint.class)
    public ResponseEntity<VideoMessageDTO> VideoLengthConstaintException (VideoLengthConstaint e){
        VideoMessageDTO videoMessageDTO = new VideoMessageDTO(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                false
        );
        return new ResponseEntity<>(videoMessageDTO, HttpStatus.NOT_FOUND);
    }

   @ExceptionHandler(MissingServletRequestPartException.class)
   public ResponseEntity<VideoMessageDTO> missingServletRequestPartException(MissingServletRequestPartException e){
       VideoMessageDTO videoMessageDTO = new VideoMessageDTO(
               e.getMessage(),
               HttpStatus.BAD_REQUEST.value(),
               false
       );
       return new ResponseEntity<>(videoMessageDTO, HttpStatus.BAD_REQUEST);
   }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<VideoMessageDTO> missingServletRequestParameterException(MissingServletRequestParameterException e){
        VideoMessageDTO videoMessageDTO = new VideoMessageDTO(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                false
        );
        return new ResponseEntity<>(videoMessageDTO, HttpStatus.BAD_REQUEST);
    }

}
