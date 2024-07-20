package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.OrganisationException;
import hng_java_boilerplate.organisation.exception.response.SuccessResponse;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.dto.GetUserDto;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.crypto.BadPaddingException;
import java.util.List;
import java.util.Optional;

@Service
public class OrganisationService {
    @Autowired
    private OrganisationRepository organisationRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public Organisation getOrganisationDetails(String orgId) throws OrganisationException{
        Organisation foundOrganisation = organisationRepository.findById(orgId)
                .orElseThrow(()-> new OrganisationException("Invalid Organisation Id"));
        return foundOrganisation;
    }

    public SuccessResponse validateAndAcceptUserToOrganisation(String OrgId){
//        This is a secured method and while i await Auth guy to come through so i can fetch the id of logged-in user
// TODO: 7/20/2024 fetch user that wants to be added to organisation from the logged in user
//        A demo user
        String userId = "6c22fdd5-c9c4-4c69-871a-4262937de72e";
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new OrganisationException("Invalid user id"));
        Organisation organisationDetails = getOrganisationDetails(OrgId);

        List<Organisation> organisations = foundUser.getOrganisations();
        organisations.add(organisationDetails);
        foundUser.setOrganisations(organisations);

        SuccessResponse response = new SuccessResponse(
                "Invitation accepted, you have been added to the organization",
                HttpStatus.OK.value()
        );

        return response;
    }




}
