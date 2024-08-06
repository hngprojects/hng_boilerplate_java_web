package hng_java_boilerplate.video.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VideoResponseDTO<T> {
    private String message;
    private int statusCode;
    private boolean success;
    private T data;
}

