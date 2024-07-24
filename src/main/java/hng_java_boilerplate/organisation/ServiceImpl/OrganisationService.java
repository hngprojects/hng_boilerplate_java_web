package hng_java_boilerplate.organisation.ServiceImpl;

import hng_java_boilerplate.exception.OrganisationNotFoundException;
import hng_java_boilerplate.exception.UserNotFoundException;
import hng_java_boilerplate.organisation.dto.AddUserToOrganisationRequestDto;
import hng_java_boilerplate.organisation.dto.OrganisationResponseDto;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public class OrganisationService {


    private final OrganisationRepository organisationRepository;


    private final UserRepository userService;


    public OrganisationResponseDto addUserToOrganisation(String orgId, AddUserToOrganisationRequestDto request, Authentication authentication) {
        User currentUser = userService.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        Organisation organisation = organisationRepository.findById(orgId)
                .orElseThrow(OrganisationNotFoundException::new);

        User userToAdd = userService.findById(currentUser.getId())
                .orElseThrow(UserNotFoundException::new);

        organisation.getUsers().add(userToAdd);
        organisationRepository.save(organisation);

        return new OrganisationResponseDto("success", "User added to organisation successfully", null);
    }

}
