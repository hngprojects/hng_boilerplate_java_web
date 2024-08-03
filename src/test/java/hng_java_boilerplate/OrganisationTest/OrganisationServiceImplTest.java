package hng_java_boilerplate.OrganisationTest;

import hng_java_boilerplate.organisation.dto.CreateOrganisationDTO;
import hng_java_boilerplate.organisation.dto.UpdateOrganisationDTO;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.exception.ResourceNotFoundException;
import hng_java_boilerplate.organisation.exception.UnauthorizedException;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.organisation.serviceImpl.OrganisationServiceImpl;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OrganisationServiceImplTest {

    @Mock
    private OrganisationRepository organisationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrganisationServiceImpl organisationService;

    private User mockUser;
    private Organisation mockOrganisation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setId("123");
        mockUser.setName("Test User");

        mockOrganisation = new Organisation();
        mockOrganisation.setId("123");
        mockOrganisation.setName("Test Org");
        mockOrganisation.setDescription("Test Description");
        mockOrganisation.setOwner(mockUser);
        mockOrganisation.setCreatedAt(LocalDateTime.now());
        mockOrganisation.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void createOrganisation_Success() {
        CreateOrganisationDTO dto = new CreateOrganisationDTO();
        dto.setName("Test Org");
        dto.setDescription("Test Description");
        dto.setEmail("test@example.com");
        dto.setIndustry("Test Industry");
        dto.setType("Test Type");
        dto.setCountry("Test Country");
        dto.setAddress("Test Address");
        dto.setState("Test State");

        when(organisationRepository.save(any(Organisation.class))).thenReturn(mockOrganisation);

        Organisation createdOrg = organisationService.createOrganisation(dto, mockUser);

        assertNotNull(createdOrg);
        assertEquals("Test Org", createdOrg.getName());
        verify(organisationRepository, times(1)).save(any(Organisation.class));
    }

    @Test
    void updateOrganisation_Success() {
        UpdateOrganisationDTO dto = new UpdateOrganisationDTO();
        dto.setName("Updated Org");
        dto.setDescription("Updated Description");

        when(organisationRepository.findById(anyString())).thenReturn(Optional.of(mockOrganisation));
        when(organisationRepository.save(any(Organisation.class))).thenReturn(mockOrganisation);

        Organisation updatedOrg = organisationService.updateOrganisation("123", dto, mockUser);

        assertNotNull(updatedOrg);
        assertEquals("Updated Org", updatedOrg.getName());
        verify(organisationRepository, times(1)).findById("123");
        verify(organisationRepository, times(1)).save(any(Organisation.class));
    }

    @Test
    void updateOrganisation_NotFound() {
        UpdateOrganisationDTO dto = new UpdateOrganisationDTO();
        dto.setName("Updated Org");
        dto.setDescription("Updated Description");

        when(organisationRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> organisationService.updateOrganisation("123", dto, mockUser));
        verify(organisationRepository, times(1)).findById("123");
        verify(organisationRepository, times(0)).save(any(Organisation.class));
    }

    @Test
    void getOrganisationById_Success() {
        when(organisationRepository.findById(anyString())).thenReturn(Optional.of(mockOrganisation));

        Organisation organisation = organisationService.getOrganisationById("123");

        assertNotNull(organisation);
        assertEquals("Test Org", organisation.getName());
        verify(organisationRepository, times(1)).findById("123");
    }

    @Test
    void getOrganisationById_NotFound() {
        when(organisationRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> organisationService.getOrganisationById("123"));
        verify(organisationRepository, times(1)).findById("123");
    }

    @Test
    void deleteOrganisation_Success() {
        when(organisationRepository.findById(anyString())).thenReturn(Optional.of(mockOrganisation));

        boolean isDeleted = organisationService.deleteOrganisation("123", mockUser);

        assertTrue(isDeleted);
        assertTrue(mockOrganisation.isDeleted());
        verify(organisationRepository, times(1)).findById("123");
        verify(organisationRepository, times(1)).save(mockOrganisation);
    }

    @Test
    void deleteOrganisation_NotFound() {
        when(organisationRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> organisationService.deleteOrganisation("123", mockUser));
        verify(organisationRepository, times(1)).findById("123");
        verify(organisationRepository, times(0)).save(any(Organisation.class));
    }

    @Test
    void getUsersInOrganisation_Success() {
        when(organisationRepository.findById(anyString())).thenReturn(Optional.of(mockOrganisation));
        when(organisationRepository.save(any(Organisation.class))).thenReturn(mockOrganisation);

        List<User> users = organisationService.getUsersInOrganisation("123", mockUser);

        assertNotNull(users);
        verify(organisationRepository, times(1)).findById("123");
    }

    @Test
    void getUsersInOrganisation_NotFound() {
        when(organisationRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> organisationService.getUsersInOrganisation("123", mockUser));
        verify(organisationRepository, times(1)).findById("123");
    }

    @Test
    void getUsersInOrganisation_Unauthorized() {
        User unauthorizedUser = new User();
        unauthorizedUser.setId("456");
        unauthorizedUser.setName("Unauthorized User");

        when(organisationRepository.findById(anyString())).thenReturn(Optional.of(mockOrganisation));

        assertThrows(UnauthorizedException.class, () -> organisationService.getUsersInOrganisation("123", unauthorizedUser));
        verify(organisationRepository, times(1)).findById("123");
    }
}
