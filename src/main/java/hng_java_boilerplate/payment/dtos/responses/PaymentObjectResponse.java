package hng_java_boilerplate.payment.dtos.responses;

import lombok.*;


@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Getter
public class PaymentObjectResponse<T> {

    private String status;

    private String message;

    private T data;







}
