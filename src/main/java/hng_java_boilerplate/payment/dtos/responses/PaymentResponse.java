package hng_java_boilerplate.payment.dtos.responses;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class PaymentResponse {

    private String reference;
    private String amount;
    private String currency;
    private String status;
    private String paymentChannel;
    private String paymentProvider;


}
