package hng_java_boilerplate.payment.service.userPayment;

import hng_java_boilerplate.payment.dtos.responses.PaymentObjectResponse;

public interface UserPaymentService {

    PaymentObjectResponse<?> getPaymentsByUserEmail(String email);

    PaymentObjectResponse<?> findPaymentByReference(String reference);

}
