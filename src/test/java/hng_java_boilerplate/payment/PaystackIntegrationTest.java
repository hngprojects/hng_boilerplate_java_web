package hng_java_boilerplate.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.payment.controller.PaymentController;
import hng_java_boilerplate.payment.dtos.reqests.PaymentRequest;
import hng_java_boilerplate.payment.dtos.responses.PaymentInitializationResponse;
import hng_java_boilerplate.payment.dtos.responses.PaymentObjectResponse;
import hng_java_boilerplate.payment.dtos.responses.PaymentVerificationResponse;
import hng_java_boilerplate.payment.entity.Payment;
import hng_java_boilerplate.payment.enums.PaymentStatus;
import hng_java_boilerplate.payment.repositories.PaymentRepository;
import hng_java_boilerplate.payment.service.PaymentService;
import hng_java_boilerplate.payment.service.PaystackServiceImpl;
import hng_java_boilerplate.user.dto.request.SignupDto;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.enums.Role;
import hng_java_boilerplate.user.service.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PaystackIntegrationTest {


    @Value("${paystack.secret.key}")
    private String paystackSecretKey;

    @MockBean
    private PaymentRepository paymentRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    private MockRestServiceServer mockServer;

    private final RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    private String reference;


    @Test
    @Order(1)
    @DisplayName("Initiate payment test")
    public void initiatePaymentTest() throws JsonProcessingException {
        User user = new User();
        user.setEmail("test@example.com");
        when(userService.getLoggedInUser()).thenReturn(user);

        PaymentRequest request = new PaymentRequest();
        request.setAmount(1000);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + paystackSecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("email", user.getEmail());
        requestPayload.put("amount", request.getAmount() * 100);
        requestPayload.put("channels", Arrays.asList("card", "bank", "ussd", "qr", "bank_transfer"));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(requestPayload);
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonPayload, headers);

        mockServer.expect(requestTo("https://api.paystack.co/transaction/initialize"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"status\": true, \"data\": {\"authorization_url\": \"https://paystack.com/pay/abc123\", \"reference\": \"67890\"}}", MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = restTemplate.exchange("https://api.paystack.co/transaction/initialize", HttpMethod.POST, httpEntity, String.class);
        ResponseEntity<?> responseEntity = paymentService.initiatePayment(request);

        PaymentInitializationResponse res = (PaymentInitializationResponse) responseEntity.getBody();

        reference = (String) res.getData().get("reference");
        System.out.println("rr -- " + res.getData().get("reference"));
        assertEquals(response.getStatusCode(), responseEntity.getStatusCode());
        assertNotNull(response);
        assertEquals("Paystack Payment Successfully Initialized", res.getMessage());
        assertEquals("200", res.getStatus_code());
        assertNotNull(res.getData());
    }


    @Test
    @DisplayName("Verify payment with reference test")
    void testVerifyPayment() {
        User user = new User();
        user.setEmail("test@example.com");

//        String reference = "nu1qfwb6fq";

        when(userService.getLoggedInUser()).thenReturn(user);

        String paystackResponse = "{ \"data\": { \"status\": \"success\", \"channel\": \"card\", \"amount\": 1000, \"currency\": \"NGN\", \"paid_at\": \"2023-07-28T16:20:15.678480\" } }";
        mockServer.expect(requestTo("https://api.paystack.co/transaction/verify/" + reference))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(paystackResponse, MediaType.APPLICATION_JSON));

        Payment payment = new Payment();
        payment.setTransactionReference(reference);
        payment.setUserEmail(user.getEmail());

        when(paymentRepository.findByUserEmailAndTransactionReference(anyString(), anyString()))
                .thenReturn(Optional.of(payment));

        System.out.println("ref here -- " + reference);
        ResponseEntity<?> response = paymentService.verifyPayment("nu1qfwb6fq");
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }


    @Test
    @DisplayName("Get all payment for a user test")
    void testGetPaymentsByUserEmail() {
        String email = "test@example.com";

        Payment payment = new Payment();
        payment.setTransactionReference("ref123");
        payment.setAmount(new BigDecimal("1000.00"));
        payment.setUserEmail(email);
        payment.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        payment.setCurrency("NGN");
        payment.setPaymentChannel("card");
        payment.setInitiatedAt(LocalDateTime.now());
        payment.setCompletedAt(LocalDateTime.now());

        when(paymentRepository.findByUserEmail(email)).thenReturn(Collections.singletonList(payment));

        PaymentObjectResponse<?> response = paymentService.getPaymentsByUserEmail(email);

        assertEquals("200", response.getStatus_code());
        assertEquals("User payments successfully fetched", response.getMessage());
    }

    @Test
    @DisplayName("Get payment by reference test")
    void testFindPaymentByReference() {
        String reference = "ref123";

        Payment payment = new Payment();
        payment.setTransactionReference(reference);
        payment.setAmount(new BigDecimal("1000.00"));
        payment.setUserEmail("test@example.com");
        payment.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        payment.setCurrency("NGN");
        payment.setPaymentChannel("card");
        payment.setInitiatedAt(LocalDateTime.now());
        payment.setCompletedAt(LocalDateTime.now());

        when(paymentRepository.findByTransactionReference(reference)).thenReturn(Optional.of(payment));

        PaymentObjectResponse<?> response = paymentService.findPaymentByReference(reference);

        assertEquals("200", response.getStatus_code());
        assertEquals("Payment fetched successfully", response.getMessage());
    }


}
