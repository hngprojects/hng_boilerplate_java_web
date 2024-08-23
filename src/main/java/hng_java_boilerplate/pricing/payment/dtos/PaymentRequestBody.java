package hng_java_boilerplate.pricing.payment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record PaymentRequestBody(
        @NotBlank(message = "Interval can not be blank")
        String interval,

        @JsonProperty("plan_id")
        @NotBlank(message = "Plan id can not be blank")
        String planId,

        @NotBlank(message = "User email can not be blank")
        @JsonProperty("user_email")
        String userEmail,

        @NotBlank(message = "Name must not be blank")
        @JsonProperty("user_name")
        String userName
) {
}
