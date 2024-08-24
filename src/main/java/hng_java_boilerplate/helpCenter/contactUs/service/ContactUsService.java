package hng_java_boilerplate.helpCenter.contactUs.service;

import hng_java_boilerplate.helpCenter.contactUs.dto.request.ContactUsRequest;
import hng_java_boilerplate.helpCenter.contactUs.dto.response.ContactUsResponse;
import hng_java_boilerplate.helpCenter.contactUs.dto.response.CustomResponse;

import java.util.List;

public interface ContactUsService {
    CustomResponse processContactMessage(ContactUsRequest request);
    List<ContactUsResponse> getAllContacts();
}
