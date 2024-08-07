package hng_java_boilerplate.user.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class ForgotPasswordRequest {

    private String email;

}
