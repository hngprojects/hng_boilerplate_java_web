package hng_java_boilerplate.user.dto.response;

import lombok.*;

import java.util.Map;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Response<T> {

    private String status;
    private String message;
    private T data;

}
