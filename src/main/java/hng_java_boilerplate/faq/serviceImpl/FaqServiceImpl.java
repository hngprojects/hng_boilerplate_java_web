package hng_java_boilerplate.faq.serviceImpl;

import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.faq.dto.request.FaqRequest;
import hng_java_boilerplate.faq.dto.response.CustomResponse;
import hng_java_boilerplate.faq.dto.response.FaqResponse;
import hng_java_boilerplate.faq.entity.FAQ;
import hng_java_boilerplate.faq.repository.FaqRepository;
import hng_java_boilerplate.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {
    private final FaqRepository faqRepository;
    @Override
    public FaqResponse createFaq(FaqRequest request) {
        FAQ faq = new FAQ();
        faq.setQuestion(request.getQuestion());
        faq.setAnswer(request.getAnswer());

        faqRepository.saveAndFlush(faq);
        return new FaqResponse("success", faq.getId(), faq.getQuestion(), faq.getAnswer());
    }

    @Override
    public List<FaqResponse> getFaqs() {
        List<FAQ> faqs =  faqRepository.findAll();

        return faqs.stream().map((faq) ->
                new FaqResponse("success",
                        faq.getId(),
                        faq.getQuestion(),
                        faq.getAnswer())).toList();
    }

    @Override
    public CustomResponse updateFaq(FaqRequest request, String faqId) {
        FAQ faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new BadRequestException("invalid request"));

        if (request.getAnswer() == null && request.getQuestion() == null) {
            throw new BadRequestException("you need to provide field to update");
        }

        faq.setQuestion(request.getQuestion() != null ? request.getQuestion() : faq.getQuestion());
        faq.setAnswer(request.getAnswer() != null ? request.getAnswer() : faq.getAnswer());

        faqRepository.save(faq);
        return new CustomResponse("success", "faq updated successfully");
    }

    @Override
    public CustomResponse deleteFaq(String faqId) {
        FAQ faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new BadRequestException("invalid request"));
        faqRepository.delete(faq);
        return new CustomResponse("success", "faq deleted successfully");
    }
}
