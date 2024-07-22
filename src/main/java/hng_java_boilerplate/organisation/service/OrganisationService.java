package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.UnauthorizedException;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganisationService {

    @Autowired
    private OrganisationRepository organisationRepository;

    public void deleteOrganisation(String orgId, User user) {
        Organisation organisation = organisationRepository.findById(orgId)
                .orElseThrow(() -> new EntityNotFoundException("Invalid organization ID"));

        if (!organisation.getUsers().contains(user)) {
            throw new UnauthorizedException("User not authorized to delete this organization");
        }

        organisation.setDeleted(true);
        organisationRepository.save(organisation);
    }
}
