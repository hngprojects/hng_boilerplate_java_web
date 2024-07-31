package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.organisation.dto.CreatePermissionRequestDto;
import hng_java_boilerplate.organisation.dto.CreatePermissionResponseDto;
import hng_java_boilerplate.organisation.dto.PermissionDataDto;
import hng_java_boilerplate.organisation.entity.OrgPermission;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.OrganisationNotFoundException;
import hng_java_boilerplate.organisation.exception.PermissionNameAlreadyExistsException;
import hng_java_boilerplate.organisation.repository.OrgPermissionRepository;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrgPermissionService {
    private final OrgPermissionRepository orgPermissionRepository;
    private final OrganisationRepository organisationRepository;

    @Transactional
    public CreatePermissionResponseDto createPermission(
            CreatePermissionRequestDto request,
            String orgId
    ) {

        Organisation organisation = organisationRepository.findById(orgId).orElseThrow(
                OrganisationNotFoundException::new
        );

        orgPermissionRepository.findByName(request.name()).ifPresent(
                orgPermission -> {
                    throw new PermissionNameAlreadyExistsException();
                }
        );
        OrgPermission permission = new OrgPermission();
        permission.setName(request.name());
        permission.setDescription(request.description());

        permission.setOrganisation(organisation);
        permission.setCreatedAt(LocalDateTime.now());
        orgPermissionRepository.save(permission);

        return CreatePermissionResponseDto.builder()
                .message("Permission created successfully")
                .status_code(HttpStatus.CREATED.value())
                .data(PermissionDataDto.builder()
                        .id(permission.getId())
                        .name(permission.getName())
                        .description(permission.getDescription())
                        .created_at(permission.getCreatedAt())
                        .build())
                .build();
    }
}
