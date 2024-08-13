package hng_java_boilerplate.payment.utils;

import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import hng_java_boilerplate.payment.entity.Payment;
import hng_java_boilerplate.payment.enums.PaymentStatus;
import hng_java_boilerplate.payment.repository.PaymentRepository;
import hng_java_boilerplate.plans.entity.Plan;
import hng_java_boilerplate.plans.service.PlanService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class FulfillCheckout implements Runnable {

    private final UserService userService;
    private final PlanService planService;
    private final PaymentRepository repository;
    private final StripeObject stripeObject;
    private final String eventType;

    private final Logger logger = LoggerFactory.getLogger(FulfillCheckout.class);

    public FulfillCheckout(UserService userService, PlanService planService, PaymentRepository paymentRepository, StripeObject stripeObject, String eventType) {
        this.userService = userService;
        this.planService = planService;
        this.repository = paymentRepository;
        this.stripeObject = stripeObject;
        this.eventType = eventType;
    }

    @Override
    public void run() {
        switch (eventType) {
            case "checkout.session.completed", "checkout.session.async_payment_succeeded" -> {
                Session session = (Session) stripeObject;
                Map<String, String> metadata = ((Session) stripeObject).getMetadata();
                String planId = metadata.get("plan_id");
                String userId = metadata.get("user_id");
                String paymentId = metadata.get("payment_id");
                String status = session.getPaymentStatus();
                User user = userService.findUser(userId);
                Plan plan = planService.findOne(planId);
                user.setPlan(plan);
                userService.save(user);
                Optional<Payment> optionalPayment = repository.findById(paymentId);
                if (optionalPayment.isPresent()) {
                    Payment payment = optionalPayment.get();

                    if (status.equals("paid")) {
                        payment.setStatus(PaymentStatus.SUCCESS);
                        repository.save(payment);
                    } else if (status.equals("unpaid")) {
                        payment.setStatus(PaymentStatus.FAILED);
                        repository.save(payment);
                    }
                }
            }
            case "payment_intent.payment_failed", "checkout.session.async_payment_failed" -> {

                Map<String, String> sessionMetadata = ((PaymentIntent) stripeObject).getMetadata();
                String failedPaymentId = sessionMetadata.get("payment_id");
                Optional<Payment> optionalPayment = repository.findById(failedPaymentId);
                if (optionalPayment.isPresent()) {
                    Payment payment = optionalPayment.get();
                    payment.setStatus(PaymentStatus.FAILED);
                    repository.save(payment);
                }
            }
            case "payment_intent.succeeded" -> {
                Map<String, String> sessionMetadata = ((PaymentIntent) stripeObject).getMetadata();
                String failedPaymentId = sessionMetadata.get("payment_id");
                Optional<Payment> optionalPayment = repository.findById(failedPaymentId);
                if (optionalPayment.isPresent()) {
                    Payment payment = optionalPayment.get();
                    payment.setStatus(PaymentStatus.SUCCESS);
                    repository.save(payment);
                }
            }
            case "payment_intent.canceled" -> {
                Map<String, String> sessionMetadata = ((PaymentIntent) stripeObject).getMetadata();
                String failedPaymentId = sessionMetadata.get("payment_id");
                Optional<Payment> optionalPayment = repository.findById(failedPaymentId);
                if (optionalPayment.isPresent()) {
                    Payment payment = optionalPayment.get();
                    payment.setStatus(PaymentStatus.CANCELLED);
                    repository.save(payment);
                }
            }

            default -> logger.info("Unhandled event type {}", eventType);
        }

    }
}
