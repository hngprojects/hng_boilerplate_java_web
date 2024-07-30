package hng_java_boilerplate.payment.service.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.payment.Utils;
import hng_java_boilerplate.payment.dtos.reqests.PaymentRequest;
import hng_java_boilerplate.payment.dtos.responses.FlutterwaveResponse;
import hng_java_boilerplate.payment.dtos.responses.PaymentObjectResponse;
import hng_java_boilerplate.payment.entity.Payment;
import hng_java_boilerplate.payment.enums.PaymentProvider;
import hng_java_boilerplate.payment.enums.PaymentStatus;
import hng_java_boilerplate.payment.repository.PaymentRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Qualifier("flutterwaveService")
public class FlutterwaveServiceImpl implements PaymentService{

    private final UserService userService;

    private final PaymentRepository paymentRepository;
    private final Utils utils;

    @Value("${flutterwave.secret.key}")
    private String flutterwaveSecretKey;


    public FlutterwaveServiceImpl(UserService userService, Utils utils, PaymentRepository paymentRepository) {
        this.userService = userService;
        this.utils = utils;
        this.paymentRepository = paymentRepository;
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
            PaymentObjectResponse<?> paymentResponse = PaymentObjectResponse.builder().status_code("200").message("Flutterwave Payment Successfully initialized").data(map).build();
            return ResponseEntity.status(HttpStatus.OK).body(paymentResponse);
        } else {
            PaymentObjectResponse<?> paymentResponse = PaymentObjectResponse.builder().status_code(response.getStatusCode().toString()).message("Flutterwave Payment Initialization Failed").data(response.getBody()).build();
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
                    PaymentObjectResponse<?> paymentResponse = PaymentObjectResponse.builder().status_code("200").message("Verification successful").data(map).build();
                    return ResponseEntity.status(HttpStatus.OK).body(paymentResponse);
                } else {
                    PaymentObjectResponse<?> paymentResponse = PaymentObjectResponse.builder().status_code("400").message("Payment verification failed").build();
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(paymentResponse);
                }
            } else {
                PaymentObjectResponse<?> paymentResponse = PaymentObjectResponse.builder().status_code("404").message("Transaction not found").build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(paymentResponse);
            }
        }
        PaymentObjectResponse<?> paymentResponse = PaymentObjectResponse.builder().status_code("404").message("Payment not completed").build();
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
    public ResponseEntity<?> verifyPayment(String refernce) {
        return null;
    }



}
