package hng_java_boilerplate.squeeze.service;

import hng_java_boilerplate.squeeze.entity.SqueezeRequest;
import hng_java_boilerplate.squeeze.exceptions.DuplicateEmailException;
import hng_java_boilerplate.squeeze.repository.SqueezeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SqueezeRequestService {
    @Autowired
    private SqueezeRequestRepository repository;

    public SqueezeRequest saveSqueezeRequest(SqueezeRequest squeezeRequest) throws DuplicateEmailException {
        if (repository.existsByEmail(squeezeRequest.getEmail())) {
            throw new DuplicateEmailException("Email address already exists");
        }
        SqueezeRequest savedRequest = repository.save(squeezeRequest);
        // TODO: Call email service when it is implemented and send mail template
        return savedRequest;
    }
}
