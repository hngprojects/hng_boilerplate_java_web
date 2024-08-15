package hng_java_boilerplate.video.dto;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class VideoCompressResponse<T> {

        private String message;
        private int statusCode;
        private T data;

}
