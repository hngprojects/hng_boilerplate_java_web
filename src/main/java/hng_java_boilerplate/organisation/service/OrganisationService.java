package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.activitylog.service.ActivityLogService;
import hng_java_boilerplate.organisation.dto.ApiResponseDTO;
import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.organisation.dto.CreateOrganisationRequestDto;
import hng_java_boilerplate.organisation.dto.CreateOrganisationResponseDto;
import hng_java_boilerplate.organisation.dto.DataDto;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.OrganisationNameAlreadyExistsException;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.dto.request.GetUserDto;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganisationService {
    private final OrganisationRepository organisationRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    @Transactional
    public CreateOrganisationResponseDto create(
            CreateOrganisationRequestDto orgRequest,
            Authentication activeUser
    ) {

        if (organisationRepository.findByName(orgRequest.name()).isPresent()) {
            throw new OrganisationNameAlreadyExistsException(
                    "Sorry, an Organisation with NAME::" + orgRequest.name() + " already exists"
            );
        }

        User user  = (User) activeUser.getPrincipal();

        Organisation organisation = new Organisation();
        organisation.setName(orgRequest.name());
        organisation.setDescription(orgRequest.description());
        organisation.setEmail(orgRequest.email());
        organisation.setIndustry(orgRequest.industry());
        organisation.setType(orgRequest.type());
        organisation.setCountry(orgRequest.country());
        organisation.setAddress(orgRequest.address());
        organisation.setState(orgRequest.state());
        organisation.setUsers(List.of(user));
        // set the user that owns the organisation.
        organisation.setOwner(user.getId());

        organisationRepository.save(organisation);

        user.setOrganisations(List.of(organisation));
        userRepository.save(user);
        activityLogService.logActivity(organisation.getId(), user.getId(), "Organisation created");

        return CreateOrganisationResponseDto.builder()
                .status("success")
                .message("Organisation created successfully")
                .data(
                        DataDto.builder()
                                .id(organisation.getId())
                                .name(organisation.getName())
                                .description(organisation.getDescription())
                                .owner_id(user.getId())
                                .slug(organisation.getSlug())
                                .email(organisation.getEmail())
                                .industry(organisation.getIndustry())
                                .type(organisation.getType())
                                .country(organisation.getCountry())
                                .address(organisation.getAddress())
                                .state(organisation.getState())
                                .created_at(LocalDateTime.now())
                                .updated_at(null)
                                .build()
                )
                .status_code(201)
                .build();
    }

    public ResponseEntity<ApiResponseDTO> getOrganisationUsers(String orgId, int page, int pageSize) {
        organisationRepository.findById(orgId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organisation not found"));

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<User> users = userRepository.findByOrganisations_Id(orgId, pageable);

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.builder()
                            .statusCode(400)
                            .message("No users found for organisation with ID: " + orgId)
                            .data(null)
                            .build());
        }

        List<GetUserDto> userDtoList = users.getContent().stream()
                .map(user -> GetUserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build())
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseDTO.builder()
                        .statusCode(200)
                        .message("Users retrieved successfully for organisation with ID: " + orgId)
                        .data(userDtoList)
                        .build());
    }
}

    public Organisation getOrganisationById(String organisationId) {
        return organisationRepository.findById(organisationId)
                .orElseThrow(() -> new NotFoundException("Organization not found"));
    }
}
