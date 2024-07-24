package hng_java_boilerplate.addUserToOrganisation_Test;

import hng_java_boilerplate.exception.OrganisationNotFoundException;
import hng_java_boilerplate.exception.UserNotFoundException;
import hng_java_boilerplate.organisation.ServiceImpl.OrganisationService;
import hng_java_boilerplate.organisation.dto.AddUserToOrganisationRequestDto;
import hng_java_boilerplate.organisation.dto.OrganisationResponseDto;
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

import java.util.HashSet;
import java.util.Optional;

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

        Organisation organisation = new Organisation();
        organisation.setId(orgId);
        organisation.setUsers(new HashSet<>());

        AddUserToOrganisationRequestDto requestDto = new AddUserToOrganisationRequestDto();

        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findById("user123")).thenReturn(Optional.of(currentUser));
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
        when(userRepository.findById("user123")).thenReturn(Optional.of(currentUser));
        when(organisationRepository.findById(orgId)).thenReturn(Optional.empty());

        AddUserToOrganisationRequestDto requestDto = new AddUserToOrganisationRequestDto();

        // Act & Assert
        assertThrows(OrganisationNotFoundException.class, () ->
                organisationService.addUserToOrganisation(orgId, requestDto, authentication)
        );
    }
}
