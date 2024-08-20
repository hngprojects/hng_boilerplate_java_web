package hng_java_boilerplate.payment.service;


import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionExpireParams;
import hng_java_boilerplate.exception.exception_class.BadRequestException;
import hng_java_boilerplate.payment.dtos.PaymentRequestBody;
import hng_java_boilerplate.payment.dtos.SessionResponse;
import hng_java_boilerplate.payment.entity.Payment;
import hng_java_boilerplate.payment.enums.PaymentStatus;
import hng_java_boilerplate.payment.exceptions.PaymentNotFoundException;
import hng_java_boilerplate.payment.repository.PaymentRepository;
import hng_java_boilerplate.payment.utils.CustomerUtils;
import hng_java_boilerplate.payment.utils.FulfillCheckout;
import hng_java_boilerplate.plans.entity.Plan;
import hng_java_boilerplate.plans.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static com.stripe.param.checkout.SessionCreateParams.LineItem;
import static com.stripe.param.checkout.SessionCreateParams.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository repository;
    private final PlanService planService;


    @Value("${stripe.api.key}")
    private String API_KEY;

    @Value("${client.url}")
    private String clientBaseUrl;

    @Value("${stripe.secret.key}")
    private String API_SECRET;

    @Transactional
    public ResponseEntity<SessionResponse> createSession(PaymentRequestBody body) throws StripeException {
        Plan plan = planService.findOne(body.planId());
        if (plan.getName().equals("free")) {
            throw new BadRequestException("You can not subscribe to free plan");
        }
        Stripe.apiKey = API_KEY;


        Payment payment = new Payment();
        payment.setAmount(plan.getPrice());
        payment.setStatus(PaymentStatus.PENDING);

        Payment saved = repository.save(payment);

        Map<String, String> metadata = new HashMap<>() {{
            put("plan_id", plan.getId());
            put("user_email", body.userEmail());
            put("payment_id", saved.getId());
        }};

        Product product = new Product();
        Price price = new Price();

        price.setCurrency("usd");
        price.setUnitAmountDecimal(BigDecimal.valueOf(plan.getPrice() * 100));
        product.setName(plan.getName() + " pricing plan");
        product.setId(plan.getId());
        product.setDefaultPriceObject(price);
        getInterval(body.interval());
        Mode mode = Mode.PAYMENT;

        Customer customer = CustomerUtils.findOrCreateCustomer(body.userEmail(), body.userName());


        Builder params;
        params = builder()
                .setMode(mode)
                .setCustomer(customer.getId())
                .setSuccessUrl(clientBaseUrl + "/payment/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(clientBaseUrl + "/pricing")
                .putAllMetadata(metadata)
                .addLineItem(LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                LineItem.PriceData.builder()
                                        .setProductData(
                                                LineItem.PriceData.ProductData.builder()
                                                        .setName(product.getName())
                                                        .build()
                                        )
                                        .setCurrency(product.getDefaultPriceObject().getCurrency())
                                        .setUnitAmountDecimal(product.getDefaultPriceObject().getUnitAmountDecimal())
                                        .build()
                        )
                        .build()
                );
        params.setPaymentIntentData(
                PaymentIntentData.builder()
                        .putAllMetadata(metadata)
                        .build()
        );
        Session session = Session.create(params.build());
        HashMap<String, String> data = new HashMap<>() {{
            put("checkout_url", session.getUrl());
            put("session_id", session.getId());
        }};
        return ResponseEntity.ok(new SessionResponse("Payment initiated", 200, data));
    }

    private Object getInterval(String interval) {
        return switch (interval) {
            case "monthly", "annually", "one-time" -> null;
            default -> throw new BadRequestException("Invalid interval");
        };
    }

    @Transactional
    public ResponseEntity<?> handleWebhook(String payload, HttpServletRequest request) throws StripeException {
        Event event = Webhook.constructEvent(payload, request.getHeader("Stripe-Signature"), API_SECRET);
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();


        if (dataObjectDeserializer.getObject().isEmpty()) {
            logger.warn("Api version Error");
        } else {
            StripeObject stripeObject = dataObjectDeserializer.getObject().get();
            Thread thread = new Thread(new FulfillCheckout(repository, stripeObject, event.getType()));
            thread.start();
        }
        return ResponseEntity.ok().body(null);
    }

    public ResponseEntity<?> returnStatus(String id) throws StripeException {
        Session session = Session.retrieve(id);
        Map<String, String> metadata = session.getMetadata();
        String paymentId = metadata.get("payment_id");
        String planId = metadata.get("plan_id");
        String userEmail = metadata.get("user_email");
        Plan plan = planService.findOne(planId);
        Optional<Payment> optionalPayment = repository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            throw new PaymentNotFoundException("Payment not found");
        }
        Payment payment = optionalPayment.get();

        return ResponseEntity.ok(new HashMap<>() {{
            put("status", payment.getStatus());
            put("plan_id", plan.getId());
            put("user_email", userEmail);
        }});
    }

    public ResponseEntity<?> cancelSession(String id) throws StripeException {
        Session resource = Session.retrieve(id);
        SessionExpireParams params = SessionExpireParams.builder().build();
        resource.expire(params);

        HashMap<String, Object> response = new HashMap<>() {{
            put("status_code", 200);
            put("message", "Payment successfully cancelled");
        }};

        return ResponseEntity.ok().body(response);

    }
}
