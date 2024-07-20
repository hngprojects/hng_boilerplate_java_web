package hng_java_boilerplate.dtos.requests;

import lombok.*;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Getter
@Setter
public class RecoveryPhoneNumberRequest {

    private String recoveryPhoneNumber;

}
