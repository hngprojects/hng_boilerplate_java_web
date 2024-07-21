package hng_java_boilerplate.squeeze.service;

import hng_java_boilerplate.squeeze.entity.SqueezeRequest;
import hng_java_boilerplate.squeeze.repository.SqueezeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SqueezeRequestService {
    @Autowired
    private SqueezeRequestRepository repository;

    @Autowired
    private EmailService emailService;

    public SqueezeRequest saveSqueezeRequest(SqueezeRequest squeezeRequest) {
        SqueezeRequest savedRequest = repository.save(squeezeRequest);
//        emailService.sendTemplateEmail(squeezeRequest.getEmail());
        return savedRequest;
    }
}
