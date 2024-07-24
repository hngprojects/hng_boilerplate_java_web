package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.UnauthorizedException;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganisationService {

    private final OrganisationRepository organisationRepository;

    public OrganisationService(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }


    public Organisation createOrganisation(Organisation organisation) {
        return organisationRepository.save(organisation);
    }


    public Organisation updateOrganisation(String id, Organisation updatedOrganisation) {
        Organisation organisation = organisationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organisation not found"));
        organisation.setName(updatedOrganisation.getName());
        organisation.setDescription(updatedOrganisation.getDescription());

        return organisationRepository.save(organisation);
    }


    public List<Organisation> getAllOrganisations() {
        return organisationRepository.findAll();
    }


    public Optional<Organisation> findOrganisationById(String id) {
        return organisationRepository.findActiveById(id);
    }


    public List<Organisation> findOrganisationsByName(String name) {
        return organisationRepository.findByName(name);
    }


    public List<Organisation> getAllActiveOrganisations() {
        return organisationRepository.findAllActive();
    }


    public void softDeleteOrganisation(String id) {
        Organisation organisation = organisationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organisation not found"));
        organisation.setDeleted(true);
        organisationRepository.save(organisation);
    }
}