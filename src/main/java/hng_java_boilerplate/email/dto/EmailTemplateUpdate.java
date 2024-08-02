package hng_java_boilerplate.email.dto;

import jakarta.validation.constraints.NotBlank;

public record EmailTemplateUpdate(

        @NotBlank(message = "Name can not be blank.")
        String name,

        @NotBlank(message = "Content can not be blank")
        String content
) {
}
