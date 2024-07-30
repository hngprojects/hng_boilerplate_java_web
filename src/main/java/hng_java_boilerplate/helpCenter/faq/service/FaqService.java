package hng_java_boilerplate.helpCenter.faq.service;

import hng_java_boilerplate.helpCenter.faq.dto.request.FaqRequest;
import hng_java_boilerplate.helpCenter.faq.dto.response.CustomResponse;
import hng_java_boilerplate.helpCenter.faq.dto.response.FaqResponse;

import java.util.List;

public interface FaqService {
    FaqResponse createFaq(FaqRequest request);
    List<FaqResponse> getFaqs();
    CustomResponse updateFaq(FaqRequest request, String faqId);
    CustomResponse deleteFaq(String faqId);
}
