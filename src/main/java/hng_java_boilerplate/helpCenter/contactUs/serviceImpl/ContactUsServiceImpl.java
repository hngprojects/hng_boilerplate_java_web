package hng_java_boilerplate.helpCenter.contactUs.serviceImpl;

import hng_java_boilerplate.email.EmailServices.EmailProducerService;
import hng_java_boilerplate.helpCenter.contactUs.dto.request.ContactUsRequest;
import hng_java_boilerplate.helpCenter.contactUs.dto.response.CustomResponse;
import hng_java_boilerplate.helpCenter.contactUs.entity.Contact;
import hng_java_boilerplate.helpCenter.contactUs.repository.ContactUsRepository;
import hng_java_boilerplate.helpCenter.contactUs.service.ContactUsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactUsServiceImpl implements ContactUsService {
    private final ContactUsRepository contactUsRepository;
    private final EmailProducerService emailProducerService;

    @Value("${company.email:info-mail@company.com")
    private String companyEmail;

    @Override
    @Transactional
    public CustomResponse processContactMessage(ContactUsRequest request) {
        Contact contact = new Contact();
        contact.setEmail(request.getEmail());
        contact.setName(request.getName());
        contact.setPhone(request.getPhone_no());
        contact.setMessage(request.getMessage());

        contactUsRepository.save(contact);

        // send the message to company info email
        String subject = "New Contact Message from " + request.getName() + "\n email: " + request.getEmail();
        emailProducerService.sendEmailMessage(companyEmail, subject, request.getMessage());
        return new CustomResponse("success", "message sent");
    }
}
