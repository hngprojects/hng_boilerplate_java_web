package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.organisation.dto.CreatePermissionRequestDto;
import hng_java_boilerplate.organisation.dto.CreatePermissionResponseDto;
import hng_java_boilerplate.organisation.dto.PermissionDataDto;
import hng_java_boilerplate.organisation.entity.OrgPermission;
import hng_java_boilerplate.organisation.exception.PermissionNameAlreadyExistsException;
import hng_java_boilerplate.organisation.repository.OrgPermissionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class OrgPermissionService {
    private final OrgPermissionRepository orgPermissionRepository;

    @Transactional
    public CreatePermissionResponseDto createPermission(
            @Valid CreatePermissionRequestDto request
            ) {
        if (orgPermissionRepository.findByName(request.name()).isPresent()) {
            throw new PermissionNameAlreadyExistsException();
        }
        OrgPermission permission = new OrgPermission();
        permission.setName(request.name());
        permission.setDescription(request.description());
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
