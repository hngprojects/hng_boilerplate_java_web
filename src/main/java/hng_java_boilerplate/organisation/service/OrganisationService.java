package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.organisation.dto.request.AddUserRequest;
import hng_java_boilerplate.organisation.dto.response.AddUserResponse;

public interface OrganisationService {
    AddUserResponse addUserToOrganisation(AddUserRequest request, String org_id);
}
