package hng_java_boilerplate.payment.dtos;

import jakarta.validation.constraints.NotBlank;

public record PaymentRequestBody(
        @NotBlank(message = "Interval can not be blank")
        String interval,

        @NotBlank(message = "Plan id can not be blank")
        String planId
) {
}
