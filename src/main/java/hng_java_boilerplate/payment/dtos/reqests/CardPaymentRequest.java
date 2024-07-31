package hng_java_boilerplate.payment.dtos.reqests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class CardPaymentRequest {

    private String email;
    private String cardNumber;
    private String cardDate;
    private String cvv;
    private String cardHolderName;
    private String country;


}
