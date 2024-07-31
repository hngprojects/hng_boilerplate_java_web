package hng_java_boilerplate.payment.dtos.reqests;

import lombok.*;
import org.springframework.stereotype.Service;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Setter
@Getter
public class SubscriptionPlanRequest {

    private String organizationId;
    private String planId;
    private String fullName;
    private String billingOption;
    private String redirectUrl;

}
