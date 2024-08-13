package hng_java_boilerplate.payment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record PaymentRequestBody(
        @NotBlank(message = "Interval can not be blank")
        String interval,

        @JsonProperty("plan_id")
        @NotBlank(message = "Plan id can not be blank")
        String planId
) {
}
