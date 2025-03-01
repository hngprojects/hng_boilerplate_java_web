package hng_java_boilerplate.organisation.service;

import hng_java_boilerplate.exception.ConflictException;
import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.exception.UnAuthorizedException;
import hng_java_boilerplate.organisation.dto.AddUserRequestDTO;
import hng_java_boilerplate.organisation.dto.AddUserResponseDTO;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddUsersToOrganisationServiceTest {

    @Mock
    private OrganisationRepository organisationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authenticatedUser;

    @InjectMocks
    private AddUsersToOrganisationService addUsersToOrganisationService;

    private AddUserRequestDTO orgRequest;
    private User user;

    @Test
    void testAddUserToOrganisation_OrganisationNotFound() {
        String organisationId = "b1e009c5-a197-42f9-b9a3-98fc357b5f08";
        AddUserRequestDTO orgRequest = new AddUserRequestDTO(List.of("user1", "user2", "user3"));

        when(organisationRepository.findById(organisationId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                addUsersToOrganisationService.addUserToOrganisation(organisationId, orgRequest, authenticatedUser));
        assertEquals("Organisation with id " + organisationId + " does not exist", exception.getMessage());

        verify(organisationRepository).findById(organisationId);
        verifyNoMoreInteractions(organisationRepository);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(authenticatedUser);
    }

    @Test
    void testAddUserToOrganisation_UserNotOwner() {
        String organisationId = "b1e009c5-a197-42f9-b9a3-98fc357b5f08";
        AddUserRequestDTO orgRequest = new AddUserRequestDTO(List.of("user1", "user2", "user3"));
        User activeUser = new User();
        activeUser.setId("user-123");

        Organisation organisation = new Organisation();
        organisation.setOwner("user-456");

        when(organisationRepository.findById(organisationId)).thenReturn(Optional.of(organisation));
        when(authenticatedUser.getPrincipal()).thenReturn(activeUser);

        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class, () ->
                addUsersToOrganisationService.addUserToOrganisation(organisationId, orgRequest, authenticatedUser));
        assertEquals("User is not owner of the organisation", exception.getMessage());

        verify(organisationRepository).findById(organisationId);
        verify(authenticatedUser).getPrincipal();
        verifyNoMoreInteractions(organisationRepository);
        verifyNoMoreInteractions(authenticatedUser);
        verifyNoInteractions(userRepository);
    }

    @Test
    void testAddUserToOrganisation_UserAlreadyInOrganisation() {
        String organisationId = "b1e009c5-a197-42f9-b9a3-98fc357b5f08";
        AddUserRequestDTO orgRequest = new AddUserRequestDTO(List.of("user1", "user2", "user3"));
        User activeUser = new User();
        activeUser.setId("user-123");

        User existingUser = new User();
        existingUser.setId("user1");

        Organisation organisation = new Organisation();
        organisation.setId(organisationId);
        organisation.setOwner("user-123");
        organisation.setUsers(List.of(existingUser));

        when(organisationRepository.findById(organisationId)).thenReturn(Optional.of(organisation));
        when(authenticatedUser.getPrincipal()).thenReturn(activeUser);

        ConflictException exception = assertThrows(ConflictException.class, () ->
                addUsersToOrganisationService.addUserToOrganisation(organisationId, orgRequest, authenticatedUser));
        assertEquals("User with id user1 is already in the organisation", exception.getMessage());

        verify(organisationRepository).findById(organisationId);
        verify(authenticatedUser).getPrincipal();
        verifyNoMoreInteractions(organisationRepository);
        verifyNoMoreInteractions(authenticatedUser);
        verifyNoInteractions(userRepository);
    }

    @Test
    void testAddUserToOrganisation_UserDoesNotExist() {
        String organisationId = "b1e009c5-a197-42f9-b9a3-98fc357b5f08";
        AddUserRequestDTO orgRequest = new AddUserRequestDTO(List.of("user2"));
        User activeUser = new User();
        activeUser.setId("user-123");

        Organisation organisation = new Organisation();
        organisation.setId(organisationId);
        organisation.setOwner("user-123");
        organisation.setUsers(new ArrayList<>());

        when(organisationRepository.findById(organisationId)).thenReturn(Optional.of(organisation));
        when(authenticatedUser.getPrincipal()).thenReturn(activeUser);
        when(userRepository.findById("user2")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                addUsersToOrganisationService.addUserToOrganisation(organisationId, orgRequest, authenticatedUser));
        assertEquals("User with id user2 does not exist", exception.getMessage());

        verify(organisationRepository).findById(organisationId);
        verify(authenticatedUser).getPrincipal();
        verify(userRepository).findById("user2");
        verifyNoMoreInteractions(organisationRepository);
        verifyNoMoreInteractions(authenticatedUser);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testAddUserToOrganisation_Successful() {
        String organisationId = "b1e009c5-a197-42f9-b9a3-98fc357b5f08";
        AddUserRequestDTO orgRequest = new AddUserRequestDTO(List.of("user1", "user2"));
        User activeUser = new User();
        activeUser.setId("user-123");

        Organisation organisation = new Organisation();
        organisation.setId(organisationId);
        organisation.setOwner("user-123");
        organisation.setUsers(new ArrayList<>());

        User user1 = new User();
        user1.setId("user1");
        user1.setOrganisations(new ArrayList<>());

        User user2 = new User();
        user2.setId("user2");
        user2.setOrganisations(new ArrayList<>());

        when(organisationRepository.findById(organisationId)).thenReturn(Optional.of(organisation));
        when(authenticatedUser.getPrincipal()).thenReturn(activeUser);
        when(userRepository.findById("user1")).thenReturn(Optional.of(user1));
        when(userRepository.findById("user2")).thenReturn(Optional.of(user2));

        var response = (AddUserResponseDTO) addUsersToOrganisationService.addUserToOrganisation(organisationId,
                orgRequest, authenticatedUser);

        assertEquals("success", response.status());
        assertEquals("Users added to organisation", response.message());
        assertEquals(organisationId, response.organization_id());
        assertEquals(List.of("user1", "user2"), response.users_added_to_organisation());
        assertEquals(200, response.status_code());

        verify(organisationRepository).findById(organisationId);
        verify(authenticatedUser).getPrincipal();
        verify(userRepository).findById("user1");
        verify(userRepository).findById("user2");
        verify(organisationRepository).save(organisation);
        verifyNoMoreInteractions(organisationRepository);
        verifyNoMoreInteractions(authenticatedUser);
        verifyNoMoreInteractions(userRepository);
    }
}