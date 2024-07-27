package hng_java_boilerplate.payment.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PaymentInitializationResponse {

    private String authorizationUrl;
    private String reference;

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
