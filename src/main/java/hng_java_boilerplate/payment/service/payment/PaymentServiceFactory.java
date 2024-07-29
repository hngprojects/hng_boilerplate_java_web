package hng_java_boilerplate.payment.service.payment;

import hng_java_boilerplate.payment.repositories.PaymentRepository;
import hng_java_boilerplate.user.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentServiceFactory {

//    private final UserService userService;
//    private final PaymentRepository paymentRepository;
//    private final PaystackServiceImpl paystackService;
//    private final FlutterwaveServiceImpl flutterwaveService;

    private final UserService userService;
    private final PaymentRepository paymentRepository;
    private final PaymentService paystackService;
    private final PaymentService flutterwaveService;


    public PaymentServiceFactory(UserService userService, PaymentRepository paymentRepository,
                                 @Qualifier("paystackService") PaymentService paystackService,
                                 @Qualifier("flutterwaveService") PaymentService flutterwaveService) {
        this.userService = userService;
        this.paymentRepository = paymentRepository;
        this.paystackService = paystackService;
        this.flutterwaveService = flutterwaveService;
    }

    public PaymentService getPaymentService(String provider) {
        switch (provider.toLowerCase()) {
            case "paystack":
                return paystackService;
            case "flutterwave":
                return flutterwaveService;
            default:
                throw new IllegalArgumentException("Unsupported payment provider: " + provider);
        }
    }
}
