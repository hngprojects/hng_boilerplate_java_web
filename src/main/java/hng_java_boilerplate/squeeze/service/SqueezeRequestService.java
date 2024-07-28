package hng_java_boilerplate.squeeze.service;

import hng_java_boilerplate.email.EmailServices.EmailProducerService;
import hng_java_boilerplate.squeeze.entity.SqueezeRequest;
import hng_java_boilerplate.squeeze.exceptions.DuplicateEmailException;
import hng_java_boilerplate.squeeze.repository.SqueezeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SqueezeRequestService {

    private final SqueezeRequestRepository repository;

    public SqueezeRequest saveSqueezeRequest(SqueezeRequest squeezeRequest) throws DuplicateEmailException {
        if (repository.existsByEmail(squeezeRequest.getEmail())) {
            throw new DuplicateEmailException("Email address already exists");
        }
        return repository.save(squeezeRequest);
    }
}
