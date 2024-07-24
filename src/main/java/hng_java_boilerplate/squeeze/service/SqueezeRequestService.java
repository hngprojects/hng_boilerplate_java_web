package hng_java_boilerplate.squeeze.service;

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
        SqueezeRequest savedRequest = repository.save(squeezeRequest);
        // TODO: Call email service when it is implemented and send mail template
        return savedRequest;
    }
}
