package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.exception.ConflictException;
import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.exception.UnAuthorizedException;
import hng_java_boilerplate.organisation.dto.AddUserRequestDTO;
import hng_java_boilerplate.organisation.dto.AddUserResponseDTO;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.interfaces.AddUserResponse;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddUsersToOrganisationService {
    private final OrganisationRepository organisationRepository;
    private final UserRepository userRepository;

    @Transactional
    public AddUserResponse addUserToOrganisation(
            String organisationId,
            AddUserRequestDTO orgRequest,
            Authentication authenticatedUser
    ) {
        Organisation organisation = organisationRepository.findById(organisationId).
                orElseThrow(() -> new NotFoundException("Organisation with id " + organisationId + " does not exist"));

        User user = (User) authenticatedUser.getPrincipal();

        if (!organisation.getOwner().equals(user.getId())) {
            throw new UnAuthorizedException("User is not owner of the organisation");
        }

        List<String> addedUserIds = new ArrayList<>();

        for (String userId : orgRequest.user_ids()) {
            if (organisation.getUsers().stream().anyMatch(user1 -> user1.getId().equals(userId))) {
                throw new ConflictException("User with id " + userId + " is already in the organisation");
            }

            var userExists = userRepository.findById(userId);
            if (userExists.isEmpty()) {
                throw new NotFoundException("User with id " + userId + " does not exist");
            }

            organisation.getUsers().add(userExists.get());
            userExists.get().getOrganisations().add(organisation);
            addedUserIds.add(userId);
        }

        organisationRepository.save(organisation);

        return AddUserResponseDTO.builder()
                .status("success")
                .message("Users added to organisation")
                .organization_id(organisation.getId())
                .users_added_to_organisation(addedUserIds)
                .status_code(HttpStatus.OK.value())
                .build();
    }
}
