package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.organisation.common.PageResponse;
import hng_java_boilerplate.organisation.mapper.OrganisationMapper;
import hng_java_boilerplate.organisation.dto.*;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.NotPermittedException;
import hng_java_boilerplate.organisation.exception.OrganisationNameAlreadyExistsException;
import hng_java_boilerplate.organisation.exception.OrganisationNotFoundException;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.organisation.specification.UserSpecification;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.exception.UserNotFoundException;
import hng_java_boilerplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrganisationService {
    private final OrganisationRepository organisationRepository;
    private final UserRepository userRepository;
    private final OrganisationMapper organisationMapper;

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

        organisationMapper.setCreateOrganisationFields(organisation, orgRequest, user);

        organisationRepository.save(organisation);

        user.setOrganisations(List.of(organisation));
        userRepository.save(user);

        return CreateOrganisationResponseDto.builder()
                .status("success")
                .message("Organisation created successfully")
                .data(
                        organisationMapper.toOrgDataDto(organisation, user)
                )
                .status_code(201)
                .build();
    }

    @Transactional
    public UpdateOrganisationResponseDto update(
            UpdateOrganisationRequestDto orgRequest,
            String orgId,
            Authentication activeUser
    ) {
        Organisation organisation = organisationRepository.findById(orgId).orElseThrow(
                OrganisationNotFoundException::new
        );

        User user = (User) activeUser.getPrincipal();
        if(!Objects.equals(user.getId(), organisation.getOwner().getId())) {
            throw new NotPermittedException("You are not permitted to perform this action");
        }

        organisation.setName(orgRequest.name());
        organisation.setDescription(orgRequest.description());
        organisationRepository.save(organisation);

        return UpdateOrganisationResponseDto.builder()
                .message("Organisation updated successfully")
                .org(UpdateOrgData.builder()
                        .name(organisation.getName())
                        .description(organisation.getDescription())
                        .build())
                .build();
    }

    public UserOrganisationsResponseDto userOrganisations(Authentication activeUser) {
        String email =  activeUser.getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User not found")
        );
        List<Organisation> organisations = new ArrayList<>(user.getOrganisations());

        List<OrganisationsListDto> organisationList = organisations.stream()
                .map(organisationMapper::mapToOranisationListDto)
                .toList();

        return UserOrganisationsResponseDto.builder()
                .status("success")
                .message("Organisations retrieved successfully")
                .data(OrganisationDataDto.builder()
                        .organisations(organisationList)
                        .build())
                .build();
    }

    public CreateOrganisationResponseDto findOrganisationById(String orgId) {
        Organisation organisation = organisationRepository.findById(orgId).orElseThrow(
                OrganisationNotFoundException::new
        );

        return CreateOrganisationResponseDto.builder()
                .status("success")
                .message("Organisation retrieved successfully")
                .data(
                        organisationMapper.toOrgDataDto(organisation, organisation.getOwner())
                )
                .status_code(200)
                .build();
    }

    public PageResponse<OrgUsersPaginatedResponseDto> fetchAllUsers(
            String orgId,
            int page,
            int pageSize,
            Authentication activeUser
    ) {
        Organisation organisation = organisationRepository.findById(orgId).orElseThrow(
                OrganisationNotFoundException::new
        );

        User user = (User) activeUser.getPrincipal();
        if (!Objects.equals(user.getId(), organisation.getOwner().getId())) {
            throw new NotPermittedException("User does not access to the organisation");
        }

        Pageable pageable = PageRequest.of(
                page - 1,
                pageSize
        );

        Page<User> usersPage = userRepository.findAll(new UserSpecification(organisation), pageable);

        List<OrgUsersPaginatedResponseDto> usersList = usersPage.stream()
                .map(organisationMapper::toOrgUsersPaginatedResponseDto)
                .toList();

        return new PageResponse<>(
                "success",
                "Users retrieved successfully",
                usersList,
                200,
                usersPage.getNumber(),
                usersPage.getSize(),
                usersPage.getTotalElements(),
                usersPage.getTotalPages(),
                usersPage.isFirst(),
                usersPage.isLast()
        );
    }
}