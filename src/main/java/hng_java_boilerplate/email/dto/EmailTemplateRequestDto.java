package hng_java_boilerplate.email.dto;

import jakarta.validation.constraints.NotBlank;

public record EmailTemplateRequestDto(
        @NotBlank(message = "Title can not be blank")
        String title,

        @NotBlank(message = "Template can not be blank")
        String template

) {
}
