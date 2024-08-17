package hng_java_boilerplate.helpCenter.contactUs.controller;

import hng_java_boilerplate.helpCenter.contactUs.dto.request.ContactUsRequest;
import hng_java_boilerplate.helpCenter.contactUs.dto.response.ContactUsResponse;
import hng_java_boilerplate.helpCenter.contactUs.dto.response.CustomResponse;
import hng_java_boilerplate.helpCenter.contactUs.service.ContactUsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contacts")
@Tag(name = "Contact Us")
public class ContactUsController {
    private final ContactUsService contactUsService;

    @PostMapping
    public ResponseEntity<CustomResponse> receiveContactMessage(@RequestBody @Valid ContactUsRequest request) {
        return ResponseEntity.ok(contactUsService.processContactMessage(request));
    }

    @GetMapping
    public ResponseEntity<List<ContactUsResponse>> getAllContacts() {
        return ResponseEntity.ok(contactUsService.getAllContacts());
    }
}
