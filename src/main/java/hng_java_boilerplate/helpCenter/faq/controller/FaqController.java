package hng_java_boilerplate.helpCenter.faq.controller;

import hng_java_boilerplate.helpCenter.faq.dto.request.FaqRequest;
import hng_java_boilerplate.helpCenter.faq.dto.response.CustomResponse;
import hng_java_boilerplate.helpCenter.faq.dto.response.FaqResponse;
import hng_java_boilerplate.helpCenter.faq.service.FaqService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<FaqResponse> createFaqs(@RequestBody @Valid FaqRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(faqService.createFaq(request));
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/{id}")
    public ResponseEntity<CustomResponse> updateFaq(@RequestBody FaqRequest request, @PathVariable String id) {
        return ResponseEntity.ok(faqService.updateFaq(request, id));
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse> deleteFaq(@PathVariable String id) {
        return ResponseEntity.ok(faqService.deleteFaq(id));
    }
}
