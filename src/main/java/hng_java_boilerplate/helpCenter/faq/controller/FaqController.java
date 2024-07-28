package hng_java_boilerplate.helpCenter.faq.controller;

import hng_java_boilerplate.helpCenter.faq.dto.response.FaqResponse;
import hng_java_boilerplate.helpCenter.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/faqs")
public class FaqController {
    private final FaqService faqService;

    @GetMapping
    public ResponseEntity<List<FaqResponse>> getFaqs() {
        return ResponseEntity.ok(faqService.getFaqs());
    }
}
