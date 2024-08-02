package hng_java_boilerplate.organisation.service;


import hng_java_boilerplate.organisation.common.PageResponse;

import hng_java_boilerplate.organisation.dto.*;
import hng_java_boilerplate.organisation.entity.Organisation;

import hng_java_boilerplate.organisation.exception.OrganisationNameAlreadyExistsException;

import hng_java_boilerplate.organisation.repository.OrganisationRepository;

import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.exception.UserNotFoundException;
import hng_java_boilerplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import hng_java_boilerplate.organisation.dto.CreateOrganisationRequestDto;
import hng_java_boilerplate.organisation.dto.CreateOrganisationResponseDto;
import hng_java_boilerplate.organisation.dto.DataDto;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.OrganisationNameAlreadyExistsException;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrganisationService {
    private final OrganisationRepository organisationRepository;
    private final UserRepository userRepository;





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
        organisation.setCreatedAt(LocalDateTime.now());
        organisation.setUpdatedAt(null);
        organisation.setUsers(List.of(user));
        // set the user that owns the organisation.
        organisation.setOwner(user);

        organisationRepository.save(organisation);

        user.setOrganisations(List.of(organisation));
        userRepository.save(user);

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
}

