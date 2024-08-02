package hng_java_boilerplate.email.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hng_java_boilerplate.email.entity.EmailTemplate;

public record EmailTemplateResponse(
        String message,

        @JsonProperty("status_code")
        Integer statusCode,
        EmailTemplate data

) {
}
