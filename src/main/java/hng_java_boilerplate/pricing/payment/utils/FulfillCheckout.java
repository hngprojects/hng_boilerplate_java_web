package hng_java_boilerplate.pricing.payment.utils;

import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import hng_java_boilerplate.pricing.payment.entity.Payment;
import hng_java_boilerplate.pricing.payment.enums.PaymentStatus;
import hng_java_boilerplate.pricing.payment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class FulfillCheckout implements Runnable {

    private final PaymentRepository repository;
    private final StripeObject stripeObject;
    private final String eventType;

    private final Logger logger = LoggerFactory.getLogger(FulfillCheckout.class);

    public FulfillCheckout(PaymentRepository paymentRepository, StripeObject stripeObject, String eventType) {

        this.repository = paymentRepository;
        this.stripeObject = stripeObject;
        this.eventType = eventType;
    }

    private void handlePayment(Map<String, String> metadata, PaymentStatus status) {
        String paymentId = metadata.get("payment_id");
        Optional<Payment> optionalPayment = repository.findById(paymentId);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            payment.setStatus(status);
            repository.save(payment);
        }
    }

    @Override
    public void run() {
        switch (eventType) {
            case "checkout.session.completed", "checkout.session.async_payment_succeeded" -> {
                Session session = (Session) stripeObject;
                Map<String, String> metadata = ((Session) stripeObject).getMetadata();
                String paymentId = metadata.get("payment_id");
                String status = session.getPaymentStatus();
                Optional<Payment> optionalPayment = repository.findById(paymentId);
                if (optionalPayment.isPresent()) {
                    Payment payment = optionalPayment.get();

                    if (status.equals("paid")) {
                        payment.setStatus(PaymentStatus.SUCCESS);
                        repository.save(payment);
                    }
                }
            }
            case "payment_intent.payment_failed", "checkout.session.async_payment_failed" -> {

                Map<String, String> sessionMetadata = ((PaymentIntent) stripeObject).getMetadata();
                handlePayment(sessionMetadata, PaymentStatus.FAILED);
            }
            case "payment_intent.succeeded" -> {
                Map<String, String> sessionMetadata = ((PaymentIntent) stripeObject).getMetadata();
                handlePayment(sessionMetadata, PaymentStatus.SUCCESS);
            }
            case "payment_intent.canceled" -> {
                Map<String, String> sessionMetadata = ((PaymentIntent) stripeObject).getMetadata();
                handlePayment(sessionMetadata, PaymentStatus.CANCELLED);
            }
            case "checkout.session.canceled" -> {
                Map<String, String> sessionMetadata = ((Session) stripeObject).getMetadata();
                handlePayment(sessionMetadata, PaymentStatus.CANCELLED);
            }

            default -> logger.info("Unhandled event type {}", eventType);
        }

    }
}
