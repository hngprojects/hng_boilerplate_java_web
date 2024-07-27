package hng_java_boilerplate.faq.service;

import hng_java_boilerplate.faq.dto.request.FaqRequest;
import hng_java_boilerplate.faq.dto.response.CustomResponse;
import hng_java_boilerplate.faq.dto.response.FaqResponse;

import java.util.List;

public interface FaqService {
    FaqResponse createFaq(FaqRequest request);
    List<FaqResponse> getFaqs();
    CustomResponse updateFaq(FaqRequest request, String faqId);
    CustomResponse deleteFaq(String faqId);
}
