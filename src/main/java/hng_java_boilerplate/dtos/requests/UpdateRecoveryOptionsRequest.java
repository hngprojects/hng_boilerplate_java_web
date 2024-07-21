package hng_java_boilerplate.dtos.requests;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Getter
@Setter
public class UpdateRecoveryOptionsRequest {

    private String email;
    private String phoneNumber;
//    private List<SecurityQuestionAnswer> securityQuestions;

}
