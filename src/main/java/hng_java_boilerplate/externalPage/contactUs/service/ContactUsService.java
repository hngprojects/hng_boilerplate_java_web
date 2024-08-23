package hng_java_boilerplate.externalPage.contactUs.service;

import hng_java_boilerplate.externalPage.contactUs.dto.request.ContactUsRequest;
import hng_java_boilerplate.externalPage.contactUs.dto.response.CustomResponse;

public interface ContactUsService {
    CustomResponse processContactMessage(ContactUsRequest request);
}
