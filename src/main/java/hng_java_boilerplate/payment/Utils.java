package hng_java_boilerplate.payment;

import hng_java_boilerplate.payment.dtos.responses.PaymentResponse;
import hng_java_boilerplate.payment.entity.Payment;
import hng_java_boilerplate.payment.exceptions.UserNotFoundException;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Utils {

    private final UserService userService;

    public Utils(UserService userService) {
        this.userService = userService;
    }

    public String generateTransactionReference() {
        return "TRANS_" + UUID.randomUUID();
    }

    public User validateLoggedInUser() {
        User user = userService.getLoggedInUser();
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("User not authorized");
        }
    }



    public static PaymentResponse convertToDto(Payment payment) {
        return PaymentResponse.builder()
                .reference(payment.getTransactionReference())
                .amount(payment.getAmount().toString())
                .currency(payment.getCurrency())
                .status(String.valueOf(payment.getPaymentStatus()))
                .paymentProvider(String.valueOf(payment.getProvider()))
                .paymentChannel(payment.getPaymentChannel())
                .build();
    }



}
