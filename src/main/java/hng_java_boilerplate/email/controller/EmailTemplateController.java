package hng_java_boilerplate.email.controller;


import hng_java_boilerplate.email.EmailServices.EmailTemplateService;
import hng_java_boilerplate.email.dto.EmailTemplateRequestDto;
import hng_java_boilerplate.email.dto.EmailTemplateResponse;
import hng_java_boilerplate.email.dto.EmailTemplateUpdate;
import hng_java_boilerplate.email.entity.EmailTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/email-templates")
@Tag(name = "Email Template", description = "Controller for email template")
public class EmailTemplateController {

    private final EmailTemplateService emailTemplateService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @Operation(summary = "User with admin privilege create email template")
    public ResponseEntity<EmailTemplateResponse> addEmailTemplate(@RequestBody @Valid EmailTemplateRequestDto body) {
        return emailTemplateService.create(body);
    }

    @GetMapping("{name}")
    @Operation(summary = "retrieve an email template using name of the template")
    public ResponseEntity<EmailTemplateResponse> getEmailTemplate(@PathVariable String name) {
        return emailTemplateService.getTemplate(name);
    }
    @GetMapping
    @Operation(summary = "retrieve all email template")
    public ResponseEntity<List<EmailTemplate>> getAllEmailTemplates() {
        return emailTemplateService.getAll();
    }

    @DeleteMapping("{template_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "user with admin privilege delete a template using the template id")
    public ResponseEntity<?> deleteTemplate(@PathVariable String template_id) {
        return emailTemplateService.delete(template_id);
    }

    @PatchMapping("{template_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "user with admin privilege update an email template using id of the template")
    public ResponseEntity<EmailTemplateResponse> updateEmailTemplate(@PathVariable String template_id, @RequestBody @Valid EmailTemplateUpdate emailTemplateUpdate) {
        return emailTemplateService.update(template_id, emailTemplateUpdate);
    }
}
