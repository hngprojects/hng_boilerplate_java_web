package hng_java_boilerplate.helpCenter.contactUs.serviceImpl;

import hng_java_boilerplate.email.EmailServices.EmailProducerService;
import hng_java_boilerplate.helpCenter.contactUs.dto.request.ContactUsRequest;
import hng_java_boilerplate.helpCenter.contactUs.dto.response.CustomResponse;
import hng_java_boilerplate.helpCenter.contactUs.entity.Contact;
import hng_java_boilerplate.helpCenter.contactUs.repository.ContactUsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContactUsServiceImplTest {
    @Mock
    private ContactUsRepository contactUsRepository;
    @Mock
    private EmailProducerService emailProducerService;
    @InjectMocks
    private ContactUsServiceImpl underTest;

    @Test
    void shouldProcessContactMessage() {
        ContactUsRequest request = new ContactUsRequest();
        request.setEmail("john.doe@example.com");
        request.setName("John Doe");
        request.setPhone_no("123-456-7890");
        request.setMessage("Hello, I need help with...");

        ReflectionTestUtils.setField(underTest, "companyEmail", "info-company@mail.com");

        CustomResponse response = underTest.processContactMessage(request);

        // Assert
        assertThat(response.status()).isEqualTo("success");
        assertThat(response.message()).isEqualTo("message sent");

        // Verify that the contact was saved
        ArgumentCaptor<Contact> contactCaptor = ArgumentCaptor.forClass(Contact.class);
        verify(contactUsRepository, times(1)).save(contactCaptor.capture());

        Contact savedContact = contactCaptor.getValue();
        assertThat(savedContact.getEmail()).isEqualTo(request.getEmail());
        assertThat(savedContact.getName()).isEqualTo(request.getName());
        assertThat(savedContact.getPhone()).isEqualTo(request.getPhone_no());
        assertThat(savedContact.getMessage()).isEqualTo(request.getMessage());

        // Verify that the email was sent
        verify(emailProducerService, times(1)).sendEmailMessage(
                eq("info-company@mail.com"),
                eq("New Contact Message from " + request.getName() + "\n email: " + request.getEmail()),
                eq(request.getMessage())
        );
    }
}