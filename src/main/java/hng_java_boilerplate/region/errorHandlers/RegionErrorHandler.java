package hng_java_boilerplate.region.errorHandlers;


import hng_java_boilerplate.region.dto.RegionErrorResponseDto;
import hng_java_boilerplate.region.exceptions.BadRequestException;
import hng_java_boilerplate.region.exceptions.ConflictException;
import hng_java_boilerplate.region.exceptions.RegionNotFoundException;
import hng_java_boilerplate.region.exceptions.UnauthorizedRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RegionErrorHandler {

    @ExceptionHandler(RegionNotFoundException.class)
    public ResponseEntity<RegionErrorResponseDto> handleRegionNotFoundException(RegionNotFoundException ex) {
        return new ResponseEntity<>(
                RegionErrorResponseDto.builder()
                        .statusCode("404")
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedRequestException.class)
    public ResponseEntity<RegionErrorResponseDto> handleUnauthorizedException(UnauthorizedRequestException ex) {
        return new ResponseEntity<>(
                RegionErrorResponseDto.builder()
                        .statusCode("401")
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<RegionErrorResponseDto> handleConflictException(ConflictException ex) {
        return new ResponseEntity<>(
                RegionErrorResponseDto.builder()
                        .statusCode("409")
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RegionErrorResponseDto> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(
                RegionErrorResponseDto.builder()
                        .statusCode("400")
                        .message("Bad request")
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<RegionErrorResponseDto> handleBadRequestException(BadRequestException ex) {
        return new ResponseEntity<>(RegionErrorResponseDto.builder()
                .statusCode("400")
                .message(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }
}