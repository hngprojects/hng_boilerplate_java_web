package hng_java_boilerplate.dtos.responses;


import lombok.*;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Getter
@Setter
public class ApiResponse<T>{

    private String message;
    private String statusCode;
    private String T;

}
