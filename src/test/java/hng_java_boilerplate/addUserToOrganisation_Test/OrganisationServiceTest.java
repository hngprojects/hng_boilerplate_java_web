package hng_java_boilerplate.addUserToOrganisation_Test;

import hng_java_boilerplate.exception.OrganisationNotFoundException;
import hng_java_boilerplate.exception.UserAlreadyAssignedException;
import hng_java_boilerplate.exception.UserNotFoundException;
import hng_java_boilerplate.exception.UnauthorizedAccessException;
import hng_java_boilerplate.organisation.ServiceImpl.OrganisationService;
import hng_java_boilerplate.organisation.dto.AddUserToOrganisationRequestDto;
import hng_java_boilerplate.organisation.dto.OrganisationResponseDto;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.enums.Role;
import hng_java_boilerplate.user.exception.InvalidRequestException;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrganisationServiceTest {

    @InjectMocks
    private OrganisationService organisationService;

    @Mock
    private OrganisationRepository organisationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Test
    public void testAddUserToOrganisation_Success() {
        // Arrange
        String orgId = "org123";
        User currentUser = new User();
        currentUser.setId("user123");
        currentUser.setEmail("user@example.com");
        currentUser.setUserRole(Role.ROLE_ADMIN);

        Organisation organisation = new Organisation();
        organisation.setId(orgId);
        organisation.setUsers(new HashSet<>());

        User userToAdd = new User();
        userToAdd.setId("userToAdd123");

        AddUserToOrganisationRequestDto requestDto = new AddUserToOrganisationRequestDto();
        requestDto.setUserId("userToAdd123");

        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findById("userToAdd123")).thenReturn(Optional.of(userToAdd));
        when(organisationRepository.findById(orgId)).thenReturn(Optional.of(organisation));
        when(organisationRepository.save(organisation)).thenReturn(organisation);

        // Act
        OrganisationResponseDto response = organisationService.addUserToOrganisation(orgId, requestDto, authentication);

        // Assert
        assertEquals("success", response.getStatus());
        assertEquals("User added to organisation successfully", response.getMessage());
    }

    @Test
    public void testAddUserToOrganisation_UserNotFound() {
        // Arrange
        String orgId = "org123";
        AddUserToOrganisationRequestDto requestDto = new AddUserToOrganisationRequestDto();
        requestDto.setUserId("userToAdd123");

        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () ->
                organisationService.addUserToOrganisation(orgId, requestDto, authentication)
        );
    }

    @Test
    public void testAddUserToOrganisation_OrganisationNotFound() {
        // Arrange
        String orgId = "org123";
        User currentUser = new User();
        currentUser.setId("user123");

        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findById("userToAdd123")).thenReturn(Optional.of(new User()));
        when(organisationRepository.findById(orgId)).thenReturn(Optional.empty());

        AddUserToOrganisationRequestDto requestDto = new AddUserToOrganisationRequestDto();
        requestDto.setUserId("userToAdd123");

        // Act & Assert
        assertThrows(OrganisationNotFoundException.class, () ->
                organisationService.addUserToOrganisation(orgId, requestDto, authentication)
        );
    }

    @Test
    public void testAddUserToOrganisation_InvalidRequest() {
        // Arrange
        String orgId = "org123";
        User currentUser = new User();
        currentUser.setId("user123");

        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));

        AddUserToOrganisationRequestDto requestDto = new AddUserToOrganisationRequestDto();

        // Act & Assert
        assertThrows(InvalidRequestException.class, () ->
                organisationService.addUserToOrganisation(orgId, requestDto, authentication)
        );
    }

    @Test
    public void testAddUserToOrganisation_UserAlreadyAssigned() {
        // Arrange
        String orgId = "org123";
        User currentUser = new User();
        currentUser.setId("user123");
        currentUser.setEmail("user@example.com");
        currentUser.setUserRole(Role.ROLE_ADMIN);

        Organisation organisation = new Organisation();
        organisation.setId(orgId);
        organisation.setUsers(new HashSet<>());

        User userToAdd = new User();
        userToAdd.setId("userToAdd123");

        organisation.getUsers().add(userToAdd);

        AddUserToOrganisationRequestDto requestDto = new AddUserToOrganisationRequestDto();
        requestDto.setUserId("userToAdd123");

        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findById("userToAdd123")).thenReturn(Optional.of(userToAdd));
        when(organisationRepository.findById(orgId)).thenReturn(Optional.of(organisation));

        // Act & Assert
        assertThrows(UserAlreadyAssignedException.class, () ->
                organisationService.addUserToOrganisation(orgId, requestDto, authentication)
        );
    }

    @Test
    public void testAddUserToOrganisation_UnauthorizedAccess() {
        // Arrange
        String orgId = "org123";
        User currentUser = new User();
        currentUser.setId("user123");
        currentUser.setEmail("user@example.com");
        currentUser.setUserRole(Role.ROLE_USER); // Not an admin or super admin

        Organisation organisation = new Organisation();
        organisation.setId(orgId);
        organisation.setUsers(new HashSet<>());

        User userToAdd = new User();
        userToAdd.setId("userToAdd123");

        AddUserToOrganisationRequestDto requestDto = new AddUserToOrganisationRequestDto();
        requestDto.setUserId("userToAdd123");

        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findById("userToAdd123")).thenReturn(Optional.of(userToAdd));
        when(organisationRepository.findById(orgId)).thenReturn(Optional.of(organisation));

        // Act & Assert
        assertThrows(UnauthorizedAccessException.class, () ->
                organisationService.addUserToOrganisation(orgId, requestDto, authentication)
        );
    }
}
