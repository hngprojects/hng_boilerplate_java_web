package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.organisation.dto.CreateOrganisationDTO;
import hng_java_boilerplate.organisation.dto.CreateRoleDTO;
import hng_java_boilerplate.organisation.dto.UpdateOrganisationDTO;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.organisation.entity.Role;

import java.util.List;
import java.util.Set;

public interface OrganisationServices {
    Organisation createOrganisation(CreateOrganisationDTO dto, User owner);
     boolean deleteOrganisation(String orgId, User owner);

     boolean removeUserFromOrganisation(String orgId, String userId, User owner);
    Organisation updateOrganisation(String orgId, UpdateOrganisationDTO dto, User owner);

     Organisation getOrganisationById(String orgId);
    List<User> getUsersInOrganisation(String orgId,User requester);
    boolean sendInviteToUser(String orgId, String email, User owner);
    public Organisation acceptInvitation(String token, User user);
    public Role createRoleInOrganisation(String orgId, CreateRoleDTO dto, User owner);
    public List<Role> getAllRolesInOrganisation(String orgId, User requester);
    Role getRoleDetails(String orgId, String roleId, User owner);
    Role updateRoleInOrganisation(String orgId, String roleId, Role updatedRoleData, User owner);


    Role updateRolePermissions(String orgId, String roleId, Set<String> permissionIds, User owner);
}
