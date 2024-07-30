package hng_java_boilerplate.profile.errorHandlers;

import hng_java_boilerplate.profile.dto.response.ProfileErrorResponseDto;
import hng_java_boilerplate.profile.exceptions.InternalServerErrorException;
import hng_java_boilerplate.profile.exceptions.NotFoundException;
import hng_java_boilerplate.profile.exceptions.UnauthorizedException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order(1)
@ControllerAdvice
public class ProfileErrorHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ProfileErrorResponseDto> handleUnauthorizedException(UnauthorizedException ex) {
        return new ResponseEntity<>(ProfileErrorResponseDto.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .error("Unauthorized")
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProfileErrorResponseDto> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ProfileErrorResponseDto.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .error("Not found")
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ProfileErrorResponseDto> handleInternalServerErrorException(InternalServerErrorException ex) {
        return new ResponseEntity<>(ProfileErrorResponseDto.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .error("Internal server error")
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
