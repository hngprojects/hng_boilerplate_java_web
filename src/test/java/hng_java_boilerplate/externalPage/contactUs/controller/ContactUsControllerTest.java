package hng_java_boilerplate.externalPage.contactUs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.externalPage.contactUs.dto.request.ContactUsRequest;
import hng_java_boilerplate.externalPage.contactUs.serviceImpl.ContactUsServiceImpl;
import hng_java_boilerplate.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactUsController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContactUsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ContactUsServiceImpl contactusService;
    @MockBean
    private JwtUtils jwtUtils;
    private ContactUsRequest contactMessageDto;

    @BeforeEach
    void setUp() {
        contactMessageDto = new ContactUsRequest();
        contactMessageDto.setName("John Doe");
        contactMessageDto.setEmail("john.doe@example.com");
        contactMessageDto.setPhone_no("123-456-7890");
        contactMessageDto.setMessage("Hello, I need help with...");
    }

    @Test
    void shouldCreateMockMvc() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void shouldReceiveContactMessage() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestString = objectMapper.writeValueAsString(contactMessageDto);

        mockMvc.perform(post("/api/v1/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isOk());

        verify(contactusService, times(1)).processContactMessage(any(ContactUsRequest.class));
    }

    @Test
    void processContactMessageWhenNoBody() throws Exception {
        mockMvc.perform(post("/api/v1/contacts")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }
}