package hng_java_boilerplate.SMSTests.unit_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import hng_java_boilerplate.SMS.SmsException.BadTwilioCredentialsException;
import hng_java_boilerplate.SMS.SmsException.PhoneNumberOrMessageNotValidException;
import hng_java_boilerplate.SMS.dto.SmsRequestDto;
import hng_java_boilerplate.SMS.dto.SmsResponseDto;
import hng_java_boilerplate.SMS.entity.SMS;
import hng_java_boilerplate.SMS.repository.SMSRepository;
import hng_java_boilerplate.SMS.serviceImpl.RabbitMQProducer;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RabbitMQProducerUnitTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private SMSRepository smsRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private RabbitMQProducer rabbitMQProducer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(rabbitMQProducer, "rabbitmq_exchange_key", "exchange_key");
        ReflectionTestUtils.setField(rabbitMQProducer, "rabbitmq_routing_key", "routing_key");

        Twilio.init("dummyAccountSid", "dummyAuthToken"); // Mock initialization
    }

    @Test
    public void testSendSMS_Success() throws Exception {
        SmsRequestDto smsRequestDto = new SmsRequestDto("+1234567890", "Hello World");
        User user = new User();
        user.setId(String.valueOf(1L));

        // Mock static method Message.creator
        try (MockedStatic<Message> mockedMessage = mockStatic(Message.class)) {
            MessageCreator messageCreatorMock = mock(MessageCreator.class);
            Message messageMock = mock(Message.class);

            // Mock Message.creator to return the messageCreatorMock
            mockedMessage.when(() -> Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString()))
                    .thenReturn(messageCreatorMock);

            // Mock create method to return the messageMock
            when(messageCreatorMock.create()).thenReturn(messageMock);
            when(objectMapper.writeValueAsString(any(Message.class))).thenReturn("messageJson");
            when(userService.getLoggedInUser()).thenReturn(user);
            when(smsRepository.save(any(SMS.class))).thenReturn(new SMS());

            // Call the method
            ResponseEntity<?> response = rabbitMQProducer.sendSMS(smsRequestDto);

            // Verify results
            assertEquals(HttpStatus.OK, response.getStatusCode());
            SmsResponseDto responseBody = (SmsResponseDto) response.getBody();
            assertEquals("success", responseBody.getStatus());
            assertEquals(200, responseBody.getStatus_code());
            assertEquals("SMS sent successfully.", responseBody.getMessage());

            // Verify interactions
            verify(rabbitTemplate).convertAndSend(anyString(), anyString(), eq("messageJson"));
            verify(smsRepository).save(any(SMS.class));
        }
    }
    @Test
    public void testSendSMS_InvalidPhoneNumber() {
        SmsRequestDto smsRequestDto = new SmsRequestDto("invalidPhoneNumber", "Hello World");

        Exception exception = assertThrows(PhoneNumberOrMessageNotValidException.class, () -> {
            rabbitMQProducer.sendSMS(smsRequestDto);
        });

        assertEquals("Valid phone number and message content must be provided.", exception.getMessage());
    }
    @Test
    public void testSendSMS_EmptyMessage() {
        SmsRequestDto smsRequestDto = new SmsRequestDto("+1234567890", "");

        Exception exception = assertThrows(PhoneNumberOrMessageNotValidException.class, () -> {
            rabbitMQProducer.sendSMS(smsRequestDto);
        });

        assertEquals("Valid phone number and message content must be provided.", exception.getMessage());
    }
    @Test
    public void testSendSMS_TwilioException() throws Exception {
        SmsRequestDto smsRequestDto = new SmsRequestDto("+1234567890", "Hello World");
        when(userService.getLoggedInUser()).thenReturn(new User());

        Exception exception = assertThrows(BadTwilioCredentialsException.class, () -> {
            rabbitMQProducer.sendSMS(smsRequestDto);
        });

        assertEquals("Failed to send SMS. Please try again later.", exception.getMessage());
    }
    @Test
    public void testSendSMS_GenericException() throws Exception {
        SmsRequestDto smsRequestDto = new SmsRequestDto("+1234567890", "Hello World");
        when(userService.getLoggedInUser()).thenReturn(new User());

        Exception exception = assertThrows(BadTwilioCredentialsException.class, () -> {
            rabbitMQProducer.sendSMS(smsRequestDto);
        });

        assertEquals("Failed to send SMS. Please try again later.", exception.getMessage());
    }
}
