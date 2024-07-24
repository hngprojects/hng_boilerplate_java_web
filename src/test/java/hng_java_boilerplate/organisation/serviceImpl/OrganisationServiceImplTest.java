package hng_java_boilerplate.organisation.serviceImpl;

import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.exception.ConflictException;
import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.organisation.dto.request.AddUserRequest;
import hng_java_boilerplate.organisation.dto.response.AddUserResponse;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganisationServiceImplTest {
    @Mock
    private OrganisationRepository organisationRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private OrganisationServiceImpl underTest;

    @Test
    void shouldAddUserToOrganisation() {
        // default organisation
        List<User> users = new ArrayList<>();
        Organisation organisation = new Organisation();
        organisation.setId("org-id");
        organisation.setName("some-organisation");
        organisation.setDescription("organisation description");
        organisation.setUsers(users);

        // default user
        User user = new User();
        user.setId("user-id");
        user.setName("john doe");
        user.setOrganisations(new ArrayList<>());
        user.setEmail("john@doe.com");

        // mock user
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // mock organisation
        when(organisationRepository.findById(organisation.getId())).thenReturn(Optional.of(organisation));

        AddUserRequest req = new AddUserRequest();
        req.setUser_id(user.getId());

        AddUserResponse res = underTest.addUserToOrganisation(req, organisation.getId());

        assertThat(res.organization_id()).isEqualTo(organisation.getId());
        assertThat(res.user_id()).isEqualTo(user.getId());

        verify(organisationRepository).findById(organisation.getId());
        verify(userRepository).findById(user.getId());
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsBlank() {
        AddUserRequest req = new AddUserRequest();
        req.setUser_id("");

        assertThatThrownBy(() -> underTest.addUserToOrganisation(req, "org-id"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("User ID is required");

        verify(organisationRepository, never()).findById(any());
        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenOrganisationNotFound() {
        AddUserRequest req = new AddUserRequest();
        req.setUser_id("user-id");

        when(organisationRepository.findById("org-id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.addUserToOrganisation(req, "org-id"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Organisation not found");

        verify(organisationRepository).findById("org-id");
        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        Organisation organisation = new Organisation();
        organisation.setId("org-id");
        organisation.setName("some-organisation");
        organisation.setDescription("organisation description");

        AddUserRequest req = new AddUserRequest();
        req.setUser_id("user-id");

        when(organisationRepository.findById("org-id")).thenReturn(Optional.of(organisation));
        when(userRepository.findById("user-id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.addUserToOrganisation(req, "org-id"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");

        verify(organisationRepository).findById("org-id");
        verify(userRepository).findById("user-id");
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyAddedToOrganisation() {
        List<User> users = new ArrayList<>();
        Organisation organisation = new Organisation();
        organisation.setId("org-id");
        organisation.setName("some-organisation");
        organisation.setDescription("organisation description");
        organisation.setUsers(users);

        User user = new User();
        user.setId("user-id");
        user.setName("john doe");
        user.setEmail("john@doe.com");

        organisation.getUsers().add(user);

        AddUserRequest req = new AddUserRequest();
        req.setUser_id("user-id");

        when(organisationRepository.findById("org-id")).thenReturn(Optional.of(organisation));
        when(userRepository.findById("user-id")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> underTest.addUserToOrganisation(req, "org-id"))
                .isInstanceOf(ConflictException.class)
                .hasMessage("User already added to organisation");

        verify(organisationRepository).findById("org-id");
        verify(userRepository).findById("user-id");
        verify(userRepository, never()).save(any());
    }
}