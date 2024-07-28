package hng_java_boilerplate.payment.dtos.responses;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;


@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Getter
public class PaymentObjectResponse<T> {

    private String status_code;

    private String message;

    private T data;







}
