package hng_java_boilerplate.helpCenter.faq.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FaqRequest {
    @NotBlank(message = "question is required")
    private String question;
    @NotBlank(message = "answer is required")
    private String answer;
    private String category;
}
