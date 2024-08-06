package hng_java_boilerplate.organisation.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@ToString
@RequiredArgsConstructor
@Setter
@Getter
public class Response<T> {

    private boolean status;
    private String message;
    private T data;
    private T pagination;



}
