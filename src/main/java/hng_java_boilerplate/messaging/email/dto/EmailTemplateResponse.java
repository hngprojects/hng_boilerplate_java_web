package hng_java_boilerplate.messaging.email.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hng_java_boilerplate.messaging.email.entity.EmailTemplate;

public record EmailTemplateResponse(
        String message,

        @JsonProperty("status_code")
        Integer statusCode,
        EmailTemplate data

) {
}
