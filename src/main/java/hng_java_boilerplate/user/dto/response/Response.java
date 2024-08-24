package hng_java_boilerplate.user.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class Response<T> {

    private String status_code;
    private String message;
    private T data;

}
