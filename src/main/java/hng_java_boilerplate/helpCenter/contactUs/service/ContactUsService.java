package hng_java_boilerplate.helpCenter.contactUs.service;

import hng_java_boilerplate.helpCenter.contactUs.dto.request.ContactUsRequest;
import hng_java_boilerplate.helpCenter.contactUs.dto.response.CustomResponse;

public interface ContactUsService {
    CustomResponse processContactMessage(ContactUsRequest request);
}
