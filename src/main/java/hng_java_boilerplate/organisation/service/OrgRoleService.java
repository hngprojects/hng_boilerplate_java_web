package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.organisation.dto.CreateRoleRequestDto;
import hng_java_boilerplate.organisation.dto.CreateRoleResponseDto;
import hng_java_boilerplate.organisation.dto.PermissionsListDto;
import hng_java_boilerplate.organisation.dto.RoleDataDto;
import hng_java_boilerplate.organisation.entity.OrgPermission;
import hng_java_boilerplate.organisation.entity.OrgRole;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.OrganisationNotFoundException;
import hng_java_boilerplate.organisation.exception.PermissionNameAlreadyExistsException;
import hng_java_boilerplate.organisation.exception.PermissionNotFoundException;
import hng_java_boilerplate.organisation.exception.RoleNameAlreadyExistsException;
import hng_java_boilerplate.organisation.repository.OrgPermissionRepository;
import hng_java_boilerplate.organisation.repository.OrgRoleRepository;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrgRoleService {
    private final OrgRoleRepository orgRoleRepository;
    private final OrgPermissionRepository orgPermissionRepository;
    private final OrganisationRepository organisationRepository;

    @Transactional
    public CreateRoleResponseDto createRole(CreateRoleRequestDto request, String orgId) {
        Organisation organisation = organisationRepository.findById(orgId)
                .orElseThrow(OrganisationNotFoundException::new);

        if (orgRoleRepository.findByName(request.name()).isPresent()) {
            throw new RoleNameAlreadyExistsException();
        }
        OrgRole role = new OrgRole();
        role.setName(request.name());
        Set<OrgPermission> permissions = new HashSet<>();
        for (String permissionId : request.permission_ids()) {
            OrgPermission permission = orgPermissionRepository.findById(permissionId)
                    .orElseThrow(PermissionNotFoundException::new);
            permissions.add(permission);
        }
        role.setPermissions(permissions);
        role.setOrganisation(organisation);
        role.setCreatedAt(LocalDateTime.now());
        orgRoleRepository.save(role);

        return CreateRoleResponseDto.builder()
                .message("Role created successfully")
                .status_code(HttpStatus.CREATED.value())
                .data(RoleDataDto.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .permissions(permissions.stream().map(
                                permission -> PermissionsListDto.builder()
                                        .id(permission.getId())
                                        .name(permission.getName())
                                        .build()
                        ).collect(Collectors.toSet()))
                        .created_at(role.getCreatedAt())
                        .build())
                .build();
    }
}
