package hng_java_boilerplate.email.controller;


import hng_java_boilerplate.email.EmailServices.EmailTemplateService;
import hng_java_boilerplate.email.dto.EmailTemplateRequestDto;
import hng_java_boilerplate.email.dto.EmailTemplateResponse;
import hng_java_boilerplate.email.dto.EmailTemplateUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/email-templates")
public class EmailTemplateController {

    private final EmailTemplateService emailTemplateService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<EmailTemplateResponse> addEmailTemplate(@RequestBody @Valid EmailTemplateRequestDto body) {
        return emailTemplateService.create(body);
    }

    @GetMapping("{name}")
    public ResponseEntity<EmailTemplateResponse> getEmailTemplate(@PathVariable String name) {
        return emailTemplateService.getTemplate(name);
    }

    @DeleteMapping("{template_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteTemplate(@PathVariable String template_id) {
        return emailTemplateService.delete(template_id);
    }

    @PatchMapping("{template_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EmailTemplateResponse> updateEmailTemplate(@PathVariable String template_id, @RequestBody @Valid EmailTemplateUpdate emailTemplateUpdate) {
        return emailTemplateService.update(template_id, emailTemplateUpdate);
    }

}
