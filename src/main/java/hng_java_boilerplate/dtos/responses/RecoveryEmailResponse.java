package hng_java_boilerplate.dtos.responses;

import lombok.*;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Getter
@Setter
public class RecoveryEmailResponse {

    private String message;

}
