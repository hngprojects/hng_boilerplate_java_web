package hng_java_boilerplate.user.dto.response;

import lombok.*;

import java.util.Map;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@ToString
public class ForgotPasswordResponse {

    private String status;
    private String message;
    private Map<String, String> data;

}
