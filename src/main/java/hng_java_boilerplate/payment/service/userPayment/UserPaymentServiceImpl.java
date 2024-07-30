package hng_java_boilerplate.payment.service.userPayment;

import hng_java_boilerplate.payment.Utils;
import hng_java_boilerplate.payment.dtos.responses.PaymentObjectResponse;
import hng_java_boilerplate.payment.dtos.responses.PaymentResponse;
import hng_java_boilerplate.payment.entity.Payment;
import hng_java_boilerplate.payment.repository.PaymentRepository;
import hng_java_boilerplate.payment.service.payment.PaystackServiceImpl;
import hng_java_boilerplate.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static hng_java_boilerplate.payment.Utils.convertToDto;

@Service
public class UserPaymentServiceImpl implements UserPaymentService {

    private final PaymentRepository paymentRepository;
    private Logger logger = LoggerFactory.getLogger(PaystackServiceImpl.class);

    private final UserService userService;


    public UserPaymentServiceImpl(UserService userService, PaymentRepository paymentRepository) {
        this.userService = userService;
        this.paymentRepository = paymentRepository;
    }


    @Override
    public PaymentObjectResponse<?> getPaymentsByUserEmail(String email) {
        List<Payment> payments = paymentRepository.findByUserEmail(email);
        List<PaymentResponse> response = payments.stream().map(Utils::convertToDto).collect(Collectors.toList());
        return PaymentObjectResponse.builder().message("User payments successfully fetched").status("200").data(response).build();
    }


    @Override
    public PaymentObjectResponse<?>  findPaymentByReference(String reference) {
        Optional<Payment> paymentOpt = paymentRepository.findByTransactionReference(reference);

        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            PaymentResponse convertedPaymentResponse = convertToDto(payment);
            return PaymentObjectResponse.builder().data(convertedPaymentResponse).status("200").message("Payment fetched successfully").build();
        } else {
            return PaymentObjectResponse.builder().status("404").message(String.format("Payment with %s not found",  reference)).build();
        }
    }

}
