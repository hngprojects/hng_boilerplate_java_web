package hng_java_boilerplate.dtos.requests;

import lombok.*;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Getter
@Setter
public class RecoveryEmailRequest {

    private String email;

}
