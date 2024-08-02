package hng_java_boilerplate.email.EmailServices;

import hng_java_boilerplate.email.dto.EmailTemplateResponse;
import hng_java_boilerplate.email.dto.EmailTemplateRequestDto;
import hng_java_boilerplate.email.dto.EmailTemplateUpdate;
import hng_java_boilerplate.email.entity.EmailTemplate;
import hng_java_boilerplate.email.enums.EmailTemplateStatus;
import hng_java_boilerplate.email.exception.EmailTemplateExists;
import hng_java_boilerplate.email.exception.EmailTemplateNotFound;
import hng_java_boilerplate.email.repository.EmailTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final EmailTemplateRepository repository;

    public ResponseEntity<EmailTemplateResponse> create(EmailTemplateRequestDto body) {
        if (repository.existsByTitle(body.title())) {
            throw new EmailTemplateExists("Email template already exists");
        }

        EmailTemplate emailTemplate = EmailTemplate.builder()
                .title(body.title())
                .template(body.template())
                .status(EmailTemplateStatus.ONLINE)
                .build();
        repository.save(emailTemplate);
        return ResponseEntity.status(201).body(new EmailTemplateResponse("Email template created successfully", 201, emailTemplate));
    }

    public ResponseEntity<EmailTemplateResponse> getTemplate(String title) {
        Optional<EmailTemplate> template = repository.findByTitle(title);
        if(template.isEmpty()) {
            throw new EmailTemplateNotFound("Email template not found");
        }
        return ResponseEntity.status(200).body(new EmailTemplateResponse("Email template found", 201, template.get()));
    }

    public ResponseEntity<?> delete(String name) {
        Optional<EmailTemplate> emailTemplate = repository.findByTitle(name);
        if (emailTemplate.isEmpty()) {
            throw new EmailTemplateNotFound("Email template not found");
        }
        repository.delete(emailTemplate.get());
        return ResponseEntity.status(204).body(null);
    }

    public ResponseEntity<EmailTemplateResponse> update(String id, EmailTemplateUpdate emailTemplateUpdate) {
        Optional<EmailTemplate> emailTemplate = repository.findById(id);
        if (emailTemplate.isEmpty()) {
            throw new EmailTemplateNotFound("Email template not found");
        }

        EmailTemplate template  = emailTemplate.get();
        template.setTitle(emailTemplateUpdate.name());
        template.setTemplate(emailTemplateUpdate.content());
        repository.save(template);

        return ResponseEntity.status(200).body(new EmailTemplateResponse("Email updated successfully", 200, template));
    }
}
