package hng_java_boilerplate.statusPage.service;

import hng_java_boilerplate.statusPage.entity.ApiStatus;
import hng_java_boilerplate.statusPage.repository.ApiStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ApiStatusServiceTest {

    @Mock
    private ApiStatusRepository apiStatusRepository;

    @InjectMocks
    private ApiStatusService apiStatusService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllApiStatuses() {
        // Arrange
        ApiStatus apiStatus = new ApiStatus();
        apiStatus.setApiGroup("Test API");
        apiStatus.setStatus(ApiStatus.Status.OPERATIONAL);
        apiStatus.setLastChecked(LocalDateTime.now());

        when(apiStatusRepository.findAll()).thenReturn(Collections.singletonList(apiStatus));

        // Act
        List<ApiStatus> result = apiStatusService.getAllApiStatuses();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test API", result.get(0).getApiGroup());
        assertEquals(ApiStatus.Status.OPERATIONAL, result.get(0).getStatus());
    }

    @Test
    public void testUpdateApiStatus_ExistingStatus() {
        // Arrange
        ApiStatus existingStatus = new ApiStatus();
        existingStatus.setApiGroup("Existing API");
        existingStatus.setStatus(ApiStatus.Status.OPERATIONAL);
        existingStatus.setLastChecked(LocalDateTime.now());

        when(apiStatusRepository.findByApiGroup("Existing API")).thenReturn(existingStatus);
        when(apiStatusRepository.save(any(ApiStatus.class))).thenReturn(existingStatus);

        ApiStatus newStatus = new ApiStatus();
        newStatus.setApiGroup("Existing API");
        newStatus.setStatus(ApiStatus.Status.DOWN);
        newStatus.setLastChecked(LocalDateTime.now());

        // Act
        ApiStatus result = apiStatusService.updateApiStatus(newStatus);

        // Assert
        assertEquals(ApiStatus.Status.DOWN, result.getStatus());
        verify(apiStatusRepository, times(1)).save(existingStatus);
    }

    @Test
    public void testUpdateApiStatus_NewStatus() {
        // Arrange
        ApiStatus newStatus = new ApiStatus();
        newStatus.setApiGroup("New API");
        newStatus.setStatus(ApiStatus.Status.OPERATIONAL);
        newStatus.setLastChecked(LocalDateTime.now());

        when(apiStatusRepository.findByApiGroup("New API")).thenReturn(null);
        when(apiStatusRepository.save(any(ApiStatus.class))).thenReturn(newStatus);

        // Act
        ApiStatus result = apiStatusService.updateApiStatus(newStatus);

        // Assert
        assertEquals("New API", result.getApiGroup());
        assertEquals(ApiStatus.Status.OPERATIONAL, result.getStatus());
        verify(apiStatusRepository, times(1)).save(newStatus);
    }
}
