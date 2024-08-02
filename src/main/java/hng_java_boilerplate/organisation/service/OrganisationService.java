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





}