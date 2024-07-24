package hng_java_boilerplate.organisation.serviceImpl;

import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.exception.ConflictException;
import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.organisation.dto.request.AddUserRequest;
import hng_java_boilerplate.organisation.dto.response.AddUserResponse;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.organisation.service.OrganisationService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganisationServiceImpl implements OrganisationService {
    private final OrganisationRepository organisationRepository;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public AddUserResponse addUserToOrganisation(AddUserRequest request, String org_id) {
        if (request.getUser_id() == null || request.getUser_id().isBlank()) {
            throw new BadRequestException("User ID is required");
        }
        Organisation organisation = organisationRepository.findById(org_id)
                .orElseThrow(() -> new NotFoundException("Organisation not found"));

        User user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (organisation.getUsers().contains(user)) {
            throw new ConflictException("User already added to organisation");
        }

        organisation.getUsers().add(user);
        user.getOrganisations().add(organisation);
        userRepository.save(user);

        return new AddUserResponse(organisation.getId(), user.getId());
    }
}
