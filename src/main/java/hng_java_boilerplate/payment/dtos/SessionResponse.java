package hng_java_boilerplate.payment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SessionResponse (

        @JsonProperty("checkout_url")
        String checkoutUrl
){
}
