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
import hng_java_boilerplate.SMS.serviceImpl.RabbitMQProducer;
import hng_java_boilerplate.SMS.repository.SMSRepository;
import hng_java_boilerplate.util.ConstantMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RabbitMQProducerUnitTest {

    @InjectMocks
    private RabbitMQProducer rabbitMQProducer;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private SMSRepository smsRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Value("${rabbitmq.exchange.key}")
    private String rabbitmq_exchange_key;

    @Value("${rabbitmq.routing.key}")
    private String rabbitmq_routing_key;

    @Value("${twilio.account.sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth.token}")
    private String AUTH_TOKEN;

    @Value("${twilio.outgoing.sms.number}")
    private String OUTGOING_SMS_NUMBER;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(rabbitMQProducer, "rabbitmq_exchange_key", "exchange_key");
        ReflectionTestUtils.setField(rabbitMQProducer, "rabbitmq_routing_key", "routing_key");
        ReflectionTestUtils.setField(rabbitMQProducer, "ACCOUNT_SID", "account_sid");
        ReflectionTestUtils.setField(rabbitMQProducer, "AUTH_TOKEN", "auth_token");
        ReflectionTestUtils.setField(rabbitMQProducer, "OUTGOING_SMS_NUMBER", "outgoing_sms_number");

        Twilio.init("account_sid", "auth_token");
    }

    @Test
    public void sendSMS_Success() throws Exception {
        SmsRequestDto smsRequestDto = new SmsRequestDto("+2349047338735", "Test message");

        try (MockedStatic<Message> mockedMessage = mockStatic(Message.class)) {
            MessageCreator messageCreatorMock = mock(MessageCreator.class);
            Message messageMock = mock(Message.class);

            mockedMessage.when(() -> Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString()))
                    .thenReturn(messageCreatorMock);
            when(messageCreatorMock.create()).thenReturn(messageMock);
            when(objectMapper.writeValueAsString(any(Message.class))).thenReturn("{}");
            when(smsRepository.save(any())).thenReturn(null);

            ResponseEntity<?> response = rabbitMQProducer.sendSMS(smsRequestDto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            SmsResponseDto responseBody = (SmsResponseDto) response.getBody();
            assertEquals(ConstantMessages.SUCCESS.getMessage(), responseBody.getStatus());
            assertEquals(200, responseBody.getStatus_code());
            assertEquals(ConstantMessages.SMS_SENT_SUCCESSFULLY.getMessage(), responseBody.getMessage());

            verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), anyString());
            verify(smsRepository, times(1)).save(any());
        }
    }
    @Test
    public void sendSMS_InvalidPhoneNumberOrMessage_ThrowsException() {
        SmsRequestDto smsRequestDto = new SmsRequestDto("invalid_number", "");

        assertThrows(PhoneNumberOrMessageNotValidException.class, () -> {
            rabbitMQProducer.sendSMS(smsRequestDto);
        });
    }
    @Test
    public void sendSMS_RepositorySaveThrowsException_ThrowsBadTwilioCredentialsException() throws Exception {
        SmsRequestDto smsRequestDto = new SmsRequestDto("+2349047338735", "Test message");

        try (MockedStatic<Message> mockedMessage = mockStatic(Message.class)) {
            MessageCreator messageCreatorMock = mock(MessageCreator.class);
            Message messageMock = mock(Message.class);

            mockedMessage.when(() -> Message.creator(any(PhoneNumber.class), any(PhoneNumber.class), anyString()))
                    .thenReturn(messageCreatorMock);
            when(messageCreatorMock.create()).thenReturn(messageMock);
            when(objectMapper.writeValueAsString(any(Message.class))).thenReturn("{}");

            when(smsRepository.save(any(SMS.class))).thenThrow(new RuntimeException("Database error"));

            assertThrows(BadTwilioCredentialsException.class, () -> {
                rabbitMQProducer.sendSMS(smsRequestDto);
            });

            verify(smsRepository, times(1)).save(any(SMS.class));
            verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), anyString());

        }
    }
}