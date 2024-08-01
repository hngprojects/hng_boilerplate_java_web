package hng_java_boilerplate.squeeze.service;

import hng_java_boilerplate.squeeze.entity.SqueezeRequest;
import hng_java_boilerplate.squeeze.exceptions.DuplicateEmailException;
import hng_java_boilerplate.squeeze.repository.SqueezeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SqueezeRequestService {

    private final SqueezeRequestRepository repository;

    public SqueezeRequest saveSqueezeRequest(SqueezeRequest squeezeRequest) throws DuplicateEmailException {
        if (repository.existsByEmail(squeezeRequest.getEmail())) {
            throw new DuplicateEmailException("EmailTemplates address already exists");
        }
        return repository.save(squeezeRequest);
    }

    public SqueezeRequest updateSqueezeRequest(SqueezeRequest squeezeRequest) {
        SqueezeRequest existingRequest = repository.findByEmail(squeezeRequest.getEmail())
                .orElseThrow(() -> new NoSuchElementException("No squeeze page record exists for the provided request body"));

        if (existingRequest.isUpdated()) {
            throw new IllegalStateException("The squeeze page record can only be updated once.");
        }

        existingRequest.setFirst_name(squeezeRequest.getFirst_name());
        existingRequest.setLast_name(squeezeRequest.getLast_name());
        existingRequest.setPhone(squeezeRequest.getPhone());
        existingRequest.setLocation(squeezeRequest.getLocation());
        existingRequest.setJob_title(squeezeRequest.getJob_title());
        existingRequest.setCompany(squeezeRequest.getCompany());
        existingRequest.setInterests(squeezeRequest.getInterests());
        existingRequest.setReferral_source(squeezeRequest.getReferral_source());
        existingRequest.setUpdated(true);

        repository.save(existingRequest);
        return existingRequest;
    }

    public List<SqueezeRequest> getAllSqueezeRequests() {
        return repository.findAll();
    }
}
