package hng_java_boilerplate.payment.service.payment.flutterwave;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.organisation.service.OrganisationService;
import hng_java_boilerplate.payment.Utils;
import hng_java_boilerplate.payment.dtos.reqests.CardPaymentRequest;
import hng_java_boilerplate.payment.dtos.reqests.PaymentRequest;
import hng_java_boilerplate.payment.dtos.reqests.SubscriptionPlanRequest;
import hng_java_boilerplate.payment.dtos.responses.FlutterwaveResponse;
import hng_java_boilerplate.payment.dtos.responses.PaymentObjectResponse;
import hng_java_boilerplate.payment.entity.Payment;
import hng_java_boilerplate.payment.enums.PaymentProvider;
import hng_java_boilerplate.payment.enums.PaymentStatus;
import hng_java_boilerplate.payment.repository.PaymentRepository;
import hng_java_boilerplate.plans.entity.Plan;
import hng_java_boilerplate.plans.repository.PlanRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FlutterwaveServiceImpl implements FlutterwaveService{

    private final UserService userService;

    private final PlanRepository planRepository;
    private final OrganisationRepository organisationRepository;

    private final PaymentRepository paymentRepository;
    private final Utils utils;

    @Value("${flutterwave.secret.key}")
    private String flutterwaveSecretKey;

    private final String BASE_API_URL = "https://api.flutterwave.com/v3";


    public FlutterwaveServiceImpl(UserService userService, Utils utils,
                                  PaymentRepository paymentRepository, OrganisationRepository organisationRepository,
                                  PlanRepository planRepository) {
        this.userService = userService;
        this.utils = utils;
        this.paymentRepository = paymentRepository;
        this.organisationRepository = organisationRepository;
        this.planRepository = planRepository;
    }

    public ResponseEntity<?> initiatePayment(PaymentRequest request) throws JsonProcessingException {
        User user = utils.validateLoggedInUser();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + flutterwaveSecretKey);
        headers.set("Content-Type", "application/json");

        String transactionReference = utils.generateTransactionReference();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("tx_ref", transactionReference);
        requestBody.put("amount", request.getAmount());
        requestBody.put("currency", "NGN");
        requestBody.put("redirect_url", "https://api-java.boilerplate.hng.tech/docs");

        Map<String, Object> customer = new HashMap<>();
        customer.put("email", user.getEmail());
        customer.put("name", user.getName());
        requestBody.put("customer", customer);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.flutterwave.com/v3/payments",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Payment payment = Payment.builder().provider(PaymentProvider.FLUTTERWAVE).initiatedAt(LocalDateTime.now())
                    .userEmail(user.getEmail()).amount(new BigDecimal(request.getAmount()))
                    .transactionReference(transactionReference).currency("NGN").build();
            paymentRepository.save(payment);

            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            String link = jsonNode.path("data").path("link").asText();

            Map<String, Object> map = new HashMap<>();
            map.put("redirect_url", link);
            map.put("reference", transactionReference);
            PaymentObjectResponse<?> paymentResponse = PaymentObjectResponse.builder().status("200").message("Flutterwave Payment Successfully initialized").data(map).build();
            return ResponseEntity.status(HttpStatus.OK).body(paymentResponse);
        } else {
            PaymentObjectResponse<?> paymentResponse = PaymentObjectResponse.builder().status(response.getStatusCode().toString()).message("Flutterwave Payment Initialization Failed").data(response.getBody()).build();
            return ResponseEntity.status(response.getStatusCode()).body(paymentResponse);
        }
    }


    public ResponseEntity<?> handlePaymentCallback(String txRef, String status, String transactionId) {
        if ("completed".equals(status) || "successful".equals(status)) {
            Optional<Payment> transactionDetailsOptional = paymentRepository.findByTransactionReference(txRef);

            if (transactionDetailsOptional.isPresent()) {
                Payment transactionDetails = transactionDetailsOptional.get();

                boolean isTransactionValid = verifyTransaction(transactionDetails, transactionId, transactionDetails.getAmount(), "NGN");
                if (isTransactionValid) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("status", status);
                    map.put("reference", txRef);
                    map.put("amount", transactionDetails.getAmount());
                    map.put("currency", "NGN");
                    PaymentObjectResponse<?> paymentResponse = PaymentObjectResponse.builder().status("200").message("Verification successful").data(map).build();
                    return ResponseEntity.status(HttpStatus.OK).body(paymentResponse);
                } else {
                    PaymentObjectResponse<?> paymentResponse = PaymentObjectResponse.builder().status("400").message("Payment verification failed").build();
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(paymentResponse);
                }
            } else {
                PaymentObjectResponse<?> paymentResponse = PaymentObjectResponse.builder().status("404").message("Transaction not found").build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(paymentResponse);
            }
        }
        PaymentObjectResponse<?> paymentResponse = PaymentObjectResponse.builder().status("404").message("Payment not completed").build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(paymentResponse);
    }

    private boolean verifyTransaction(Payment payment, String transactionId, BigDecimal amount, String currency) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + flutterwaveSecretKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        String url = "https://api.flutterwave.com/v3/transactions/" + transactionId + "/verify";

        ResponseEntity<FlutterwaveResponse> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, FlutterwaveResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            FlutterwaveResponse responseBody = response.getBody();
            if (responseBody != null) {

                payment.setPaymentStatus(PaymentStatus.SUCCESSFUL);
                payment.setCurrency(responseBody.getData().getCurrency());
                payment.setAmount(responseBody.getData().getAmount());
                paymentRepository.save(payment);
                return "successful".equals(responseBody.getData().getStatus()) &&
                        amount.compareTo(responseBody.getData().getAmount()) == 0 &&
                        currency.equals(responseBody.getData().getCurrency());
            }
        }
        return false;
    }


    @Override
    public ResponseEntity<?> verifyPayment(String reference, String status, String transactionId) {
        return handlePaymentCallback(reference,status,transactionId);
    }

    @Override
    @Transactional
    public ResponseEntity<?> createSubscriptionPlan(SubscriptionPlanRequest request) throws JsonProcessingException {
        User user = utils.validateLoggedInUser();
        var organization = organisationRepository.findById(request.getOrganizationId());
        ResponseEntity<? extends PaymentObjectResponse<? extends Object>> NOT_FOUND = validateOrganizationAndPlanId(request, organization);
        Plan plan;
        if (NOT_FOUND != null) return NOT_FOUND;
        else {
            Optional<Plan> foundPlan = planRepository.findById(request.getPlanId());
            plan = foundPlan.get();
        }
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        String url = BASE_API_URL + "/payment-plans";
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + flutterwaveSecretKey);

        Map<String, Object> planDetails = new HashMap<>();
        planDetails.put("amount", formatAmount(String.valueOf(plan.getPrice())));
        planDetails.put("name", user.getName());
        planDetails.put("interval", request.getBillingOption());

        HttpEntity<Map<String, Object>> planEntity = new HttpEntity<>(planDetails, headers);
        ResponseEntity<String> planResponse = restTemplate.exchange(url, HttpMethod.POST, planEntity, String.class);

        if (!planResponse.getStatusCode().is2xxSuccessful()) {
            return planResponse;
        }

        String planId = extractPlanId(planResponse.getBody());
        String transactionReference = utils.generateTransactionReference();
        String subscribeUrl = BASE_API_URL + "/payments";
        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("email", user.getEmail());
        customerDetails.put("full_name", user.getName());
        customerDetails.put("plan", planId);
        customerDetails.put("redirect_url", request.getRedirectUrl());
        customerDetails.put("tx_ref", transactionReference);

        Payment payment = new Payment();
        payment.setOrganizationId(request.getOrganizationId());
        payment.setInterval(request.getBillingOption());
        payment.setPlanId(planId);
        payment.setProvider(PaymentProvider.FLUTTERWAVE);
        payment.setTransactionReference(transactionReference);
        payment.setOrganizationId(organization.get().getId());
        payment.setAmount(convertToBigDecimal(request.getPlanId()));
        payment.setCurrency("NGN");
        payment.setInitiatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        HttpEntity<Map<String, Object>> subscribeEntity = new HttpEntity<>(customerDetails, headers);
        ResponseEntity<String> subscribeResponse = restTemplate.exchange(subscribeUrl, HttpMethod.POST, subscribeEntity, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(subscribeResponse.getBody());
        String redirectUrl = root.path("data").path("link").asText();

        Map<String, Object> data = new HashMap<>();
        data.put("payment_url", redirectUrl);
        PaymentObjectResponse<?> objectResponse = PaymentObjectResponse.builder().status("200").message("Payment initiated successfully").data(data).build();
        return ResponseEntity.status(HttpStatus.OK).body(objectResponse);
    }

    private ResponseEntity<? extends PaymentObjectResponse<? extends Object>> validateOrganizationAndPlanId(SubscriptionPlanRequest request, Optional<Organisation> organization) {
        if (organization.isEmpty()) {
            PaymentObjectResponse<?> objectResponse = PaymentObjectResponse.builder().status("404").message("Organization does not exist").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(objectResponse);
        }

        if (!planRepository.existsById(request.getPlanId())) {
            PaymentObjectResponse<?> objectResponse = PaymentObjectResponse.builder().status("404").message("Plan id does not exist").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(objectResponse);
        }
        return null;
    }


    private String extractPlanId(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            return root.path("data").path("id").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract plan ID", e);
        }
    }

    public BigDecimal convertToBigDecimal(String amount) {
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount: " + amount);
        }
    }



    @Override
    public ResponseEntity<String> processCardPayment(CardPaymentRequest cardPaymentRequest) {
        String url = "https://api.flutterwave.com/v3/charges?type=card";

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + flutterwaveSecretKey);

        String requestBody = "{"
                + "\"client\": \"W+C+Mi//JFO5/JIbjMu1a/V8bI4vI2wYwNN3VyLb+GiPGpNhjEGakpXboR6O2utcezwqtjpIHlDmPh93Kb0LkbTWuYvDw3fTMnlLu+uUE5UU1YQElo/nHE/BIwM4pNStcPrEYN0ecfBdn7kaJo+QKFz1lm7/HGfeJzK0O37eoCy4PJb+ZIhhoIOgoHWUVvz7IlqgE4OImagpJn4NtNeaOKHIxWBQJLUI3mBOuw5VMVk3GXAb8SZh39oW/VfeEUOVyCMxBC3ZsTVLcd5m599qh78RrcVMky1ZYxNUJCLgTQp0i0uyF5hqAQJ2dFGYBtdWKu0eaZqCkn4VjkiH1jWkrxWAKdD7HmIwhhprJ20V5qGWjX66MFYpzxdMKMQl/f1ncCfgq/jA7J2VjfyUr0zHM9LcCZ7wUiPjVCG0ejpCyvMYBZxz2x+exEd2O8tU7UR0UkJclieLjjXGsaF+Q2PVv+NzARz9MHIBzPQy5zPZKhxfJkbjX/wTVzZzaGFLxcUiXIqeXkA8znR0wpNZvXKUSwpVCA+hCAOd+9Nkofd/6fOnl2ZFkvCkTW+gF5wgSKUs9/MWcJG4Xr9lu74pEFYtqLWFSy4lPJNBDzuBvaIrfOp8bHvEch96XT3lHhBc49g1ntoFZ2Unp8tbUg99BLRj3I9LCPqN4aP4M73jXAgSLWeqgXnHYWeCsbcN5Go3ZzjQ8ng3ElKBHxcHx08t5trn1f5DkoVNoEKv/pJRniOLvONtMSrG/9jcm9LpDUKbo+NVPswax+e+r95YHsvJw8yb4dt1ZTonO+nUzs/xIYCLoYntlyl2dT58lw==\""
                + "}";

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
    }



    public String formatAmount(String amount) {
        try {
            double numericAmount = Double.parseDouble(amount);
            return String.format("$%.2f", numericAmount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount: " + amount);
        }
    }



}
