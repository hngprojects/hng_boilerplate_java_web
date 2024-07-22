package hng_java_boilerplate.SMSTests;

import hng_java_boilerplate.SMS.entity.SMS;
import hng_java_boilerplate.SMS.repository.SMSRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RabbitMQServiceImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private SMSRepository smsRepository;

    @BeforeEach
    public void setUp() {
        Mockito.reset(rabbitTemplate, smsRepository);
    }

    @Test
    public void shouldSendSMSAndSaveToDatabase() throws Exception {
        String smsRequest = "{ \"phone_number\": \"+2349047338735\", \"message\": \"Tested Approved and Trusted.\" }";

        mockMvc.perform(post("/api/v1/sms/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(smsRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.message").value("SMS sent successfully."));

        Mockito.verify(rabbitTemplate).convertAndSend(any(String.class), any(String.class));
        Mockito.verify(smsRepository).save(any(SMS.class));
    }

    @Test
    public void shouldReturnBadRequestForInvalidInput() throws Exception {
        // Arrange
        String smsRequest = "{ \"phone_number\": \"\", \"message\": \"\" }";

        // Act & Assert
        mockMvc.perform(post("/api/v1/sms/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(smsRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("unsuccessful"))
                .andExpect(jsonPath("$.status_code").value(400))
                .andExpect(jsonPath("$.message").value("Valid phone number and message content must be provided."));

        Mockito.verify(rabbitTemplate, Mockito.never()).convertAndSend(any(String.class), any(String.class));
        Mockito.verify(smsRepository, Mockito.never()).save(any(SMS.class));
    }

    @Test
    public void shouldReturnInternalServerErrorOnException() throws Exception {
        // Arrange
        String smsRequest = "{ \"phone_number\": \"+2349047338735\", \"message\": \"Tested Approved and Trusted.\" }";
        doThrow(new RuntimeException("Database error")).when(smsRepository).save(any(SMS.class));

        // Act & Assert
        mockMvc.perform(post("/api/v1/sms/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(smsRequest))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("unsuccessful"))
                .andExpect(jsonPath("$.status_code").value(500))
                .andExpect(jsonPath("$.message").value("Failed to send SMS. Please try again later."));

        Mockito.verify(rabbitTemplate).convertAndSend(any(String.class), any(String.class));
        Mockito.verify(smsRepository).save(any(SMS.class));
    }
}