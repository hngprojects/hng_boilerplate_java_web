package hng_java_boilerplate;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import hng_java_boilerplate.services.GmailService;

@WebMvcTest(ContactUsController.class)
public class ContactUsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GmailService gmailService;

    @Test
    public void sendContactUsEmail_Success() throws Exception {
        String json = "{\"email\":\"test@example.com\",\"name\":\"Test Name\",\"message\":\"Test message\"}";

        doNothing().when(gmailService).sendEmail(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/v1/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Inquiry sent successfully"))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    public void sendContactUsEmail_ValidationError() throws Exception {
        String json = "{\"name\":\"Nicanor Kyamba\",\"message\":\"Hello, this is a test message.\"}";

        mockMvc.perform(post("/api/v1/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    public void sendContactUsEmail_InternalServerError() throws Exception {
        String json = "{\"email\":\"test@example.com\",\"name\":\"Test Name\",\"message\":\"Test message\"}";

        doThrow(new RuntimeException("Internal Server Error")).when(gmailService).sendEmail(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/v1/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value(500));
    }
}