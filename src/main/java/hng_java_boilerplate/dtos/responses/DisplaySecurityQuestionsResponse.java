package hng_java_boilerplate.dtos.responses;

import lombok.*;

import java.util.Map;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Getter
@Setter
public class DisplaySecurityQuestionsResponse {

    private Map<String, String> questions;
    private String message;

}
