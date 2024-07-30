package hng_java_boilerplate.helpCenter.contactUs.controller;

import hng_java_boilerplate.helpCenter.contactUs.dto.request.ContactUsRequest;
import hng_java_boilerplate.helpCenter.contactUs.dto.response.CustomResponse;
import hng_java_boilerplate.helpCenter.contactUs.service.ContactUsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contacts")
public class ContactUsController {
    private final ContactUsService contactUsService;

    @PostMapping
    public ResponseEntity<CustomResponse> receiveContactMessage(@RequestBody @Valid ContactUsRequest request) {
        return ResponseEntity.ok(contactUsService.processContactMessage(request));
    }
}
