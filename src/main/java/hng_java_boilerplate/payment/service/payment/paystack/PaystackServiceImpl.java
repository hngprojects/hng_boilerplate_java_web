package hng_java_boilerplate.payment.service.payment.paystack;

import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.payment.dtos.reqests.SubscriptionPlanRequest;
import hng_java_boilerplate.payment.dtos.responses.PaymentObjectResponse;
import hng_java_boilerplate.payment.dtos.responses.PaymentVerificationResponse;
import hng_java_boilerplate.payment.entity.Payment;
import hng_java_boilerplate.payment.enums.PaymentProvider;
import hng_java_boilerplate.payment.dtos.reqests.PaymentRequest;
import hng_java_boilerplate.payment.dtos.responses.PaymentInitializationResponse;
import hng_java_boilerplate.payment.enums.PaymentStatus;
import hng_java_boilerplate.payment.repository.PaymentRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.exception.UserNotFoundException;
import hng_java_boilerplate.user.service.UserService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class PaystackServiceImpl implements PaystackService {

    public PaystackServiceImpl(UserService userService, PaymentRepository paymentRepository, OrganisationRepository organisationRepository) {
        this.userService = userService;
        this.paymentRepository = paymentRepository;
        this.organisationRepository = organisationRepository;
    }

    private final OrganisationRepository organisationRepository;

    private final PaymentRepository paymentRepository;
    private Logger logger = LoggerFactory.getLogger(PaystackServiceImpl.class);

    private final UserService userService;

    @Value("${paystack.secret.key}")
    private String paystackSecretKey;


    @Override
    public ResponseEntity<?> initiatePayment(PaymentRequest request) {
        User user = validateLoggedInUser();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + paystackSecretKey);
        headers.set("Content-Type", "application/json");

        JSONObject requestPayload = new JSONObject();
        requestPayload.put("email", user.getEmail().replace("\"", "").trim());
        requestPayload.put("amount", request.getAmount() * 100);
        requestPayload.put("channels", new String[]{"card", "bank", "ussd", "qr", "bank_transfer"});

        HttpEntity<String> httpEntity = new HttpEntity<>(requestPayload.toString(), headers);
        ResponseEntity<String> response = restTemplate.exchange("https://api.paystack.co/transaction/initialize", HttpMethod.POST, httpEntity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            String authorizationUrl = jsonResponse.getJSONObject("data").getString("authorization_url");
            String authorizationCode = jsonResponse.getJSONObject("data").getString("access_code");
            String reference = jsonResponse.getJSONObject("data").getString("reference");

//            Payment payment = Payment.builder().provider(PaymentProvider.PAYSTACK).initiatedAt(LocalDateTime.now()).transactionReference(reference).amount(new BigDecimal(request.getAmount())).userEmail(user.getEmail()).build();
//            paymentRepository.save(payment);
//            Map<String, Object> data = new HashMap<>();
//            data.put("authorization_url", authorizationUrl);
//            data.put("reference", reference);
//            data.put("authorizationCode", authorizationCode);
//            PaymentInitializationResponse initializationResponse = PaymentInitializationResponse.builder().message("Paystack Payment Successfully Initialized").status_code("200").data(data).build();
            return ResponseEntity.ok().body(jsonResponse);
        } else {
            logger.error("Failed to initiate payment. Status code: {}, Response body: {}", response.getStatusCode(), response.getBody());
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
    }

    private User validateLoggedInUser() {
        User user = userService.getLoggedInUser();
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("User not authorized");
        }
    }

    @Override
    public ResponseEntity<?> verifyPayment(String reference) {
        User user = validateLoggedInUser();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + paystackSecretKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("https://api.paystack.co/transaction/verify/" + reference, HttpMethod.GET, entity, String.class);

        validatePaymentVerificationResponse(reference, user.getEmail(), response);
        JSONObject jsonResponse = new JSONObject(response.getBody());

        JSONObject dataObject = jsonResponse.getJSONObject("data");
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("status", dataObject.getString("status"));
        data.put("reference", reference);
        data.put("amount", String.valueOf(dataObject.getLong("amount")));
        data.put("channel", dataObject.getString("channel"));
        data.put("currency", dataObject.getString("currency"));

        if (!dataObject.isNull("paid_at")) {
            dataObject.put("paid_at", String.valueOf(LocalDateTime.parse(dataObject.getString("paid_at").replace("Z", ""))));
        } else {
            dataObject.put("paid_at", "");
        }
        PaymentVerificationResponse verificationResponse = PaymentVerificationResponse.builder().message("Verification Successful").status_code("200").data(data).build();
        return ResponseEntity.ok(verificationResponse);
    }

    @Override
    public ResponseEntity<?> createSubscriptionPlan(SubscriptionPlanRequest subscriptionPlanRequest) {
        User user = validateLoggedInUser();
        var organization = organisationRepository.findById(subscriptionPlanRequest.getOrganizationId());
        if (organization.isEmpty()) {
            PaymentObjectResponse<?> objectResponse = PaymentObjectResponse.builder().status("404").message("Organization does not exist").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(objectResponse);
        }

        PaymentRequest paymentRequest = PaymentRequest.builder().amount(5000).provider("paystack").build();
        ResponseEntity<?> response = initiatePayment(paymentRequest);

        String customerCode = createCustomer(user.getEmail(), user.getName(), "");
        String customerPlan = createPlan("HNG Subscription", subscriptionPlanRequest.getBillingOption(), 5000);

        JSONObject jsonResponse = (JSONObject) response.getBody();

        assert jsonResponse != null;
        System.out.println("json -- " + jsonResponse);
        String authorizationUrl = jsonResponse.getJSONObject("data").getString("authorization_url");
        String authorizationCode = jsonResponse.getJSONObject("data").getString("access_code");
        String reference = jsonResponse.getJSONObject("data").getString("reference");
        String url = "https://api.paystack.co/subscription";
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        headers.set("Authorization", "Bearer " + paystackSecretKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> request = new HashMap<>();
        request.put("customer", customerCode);
        request.put("plan", customerPlan);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> httpResponse = restTemplate.postForEntity(url, httpEntity, String.class);

        if (httpResponse.getStatusCode().is2xxSuccessful()) {
            Payment payment = new Payment();
            payment.setOrganizationId(subscriptionPlanRequest.getOrganizationId());
            payment.setInterval(subscriptionPlanRequest.getBillingOption());
            payment.setPlanId(subscriptionPlanRequest.getPlanId());
            payment.setProvider(PaymentProvider.PAYSTACK);
            payment.setOrganizationId(organization.get().getId());
            payment.setAmount(BigDecimal.valueOf(5000));
            payment.setCurrency("NGN");
            payment.setInitiatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("payment_url", authorizationUrl);

        PaymentObjectResponse<?> objectResponse = PaymentObjectResponse.builder().status("200").message("Payment initiated successfully").data(data).build();
        return ResponseEntity.status(HttpStatus.OK).body(objectResponse);
    }


    private void validatePaymentVerificationResponse(String reference, String email, ResponseEntity<String> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            JSONObject data = jsonResponse.getJSONObject("data");
            String status = data.getString("status");
            Optional<Payment> payment = paymentRepository.findByUserEmailAndTransactionReference(email, reference);
            if (payment.isPresent()) {
                Payment fondPayment = payment.get();
                fondPayment.setPaymentStatus(getPaymentStatus(status));
                fondPayment.setPaymentChannel(data.getString("channel"));
                fondPayment.setAmount(BigDecimal.valueOf(data.getLong("amount")));
                fondPayment.setCurrency(data.getString("currency"));

                if (!data.isNull("paid_at")) {
                    fondPayment.setCompletedAt(LocalDateTime.parse(data.getString("paid_at").replace("Z", "")));
                } else {
                    fondPayment.setCompletedAt(null);
                }

                paymentRepository.save(fondPayment);
            }
        } else {
            logger.error("Failed to verify payment. Status code: {}, Response body: {}", response.getStatusCode(), response.getBody());
        }
    }

    private static PaymentStatus getPaymentStatus(String status) {
        PaymentStatus paymentStatus;
        switch (status) {
            case "success" -> {
                paymentStatus = PaymentStatus.SUCCESSFUL;
            }
            case "failed" -> paymentStatus = PaymentStatus.FAILED;
            case "processing" -> paymentStatus = PaymentStatus.PROCESSING;
            case "abandoned" -> paymentStatus = PaymentStatus.ABANDONED;
            case "reversed" -> paymentStatus = PaymentStatus.REVERSED;
            default -> paymentStatus = PaymentStatus.UNKNOWN;
        }
        return paymentStatus;
    }

    public String createCustomer(String email, String firstName, String lastName) {
        String url = "https://api.paystack.co/customer";
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        headers.set("Authorization", "Bearer " + paystackSecretKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> request = new HashMap<>();
        request.put("email", email);
        request.put("first_name", firstName);
//        request.put("last_name", lastName);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, httpEntity, Map.class);

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.get("status").equals(true)) {
            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            return (String) data.get("customer_code");
        } else {
            throw new RuntimeException("Customer creation failed");
        }
    }


    public String createPlan(String name, String interval, int amount) {
        String url = "https://api.paystack.co/plan";
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        headers.set("Authorization", "Bearer " + paystackSecretKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> request = new HashMap<>();
        request.put("name", name);
        request.put("interval", interval);
        request.put("amount", amount * 100); // Paystack expects amount in kobo

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, httpEntity, Map.class);

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.get("status").equals(true)) {
            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            return (String) data.get("plan_code");
        } else {
            throw new RuntimeException("Plan creation failed");
        }
    }


}
