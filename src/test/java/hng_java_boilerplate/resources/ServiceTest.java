package hng_java_boilerplate.resources;

import hng_java_boilerplate.resources.dto.ResourceRequestDto;
import hng_java_boilerplate.resources.dto.ResourceResponseDto;
import hng_java_boilerplate.resources.entity.Resources;
import hng_java_boilerplate.resources.exception.ResourcesNotFoundException;
import hng_java_boilerplate.resources.repository.ResourceRepository;
import hng_java_boilerplate.resources.service.ResourceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServiceTest {

    @Mock
    private ResourceRepository resourceRepository;
    @InjectMocks
    private ResourceServiceImpl resourceService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUnpublishResource() {

        String resourceId = "test-resource-id";
        Resources mockResource = new Resources();
        mockResource.setId(resourceId);
        mockResource.setPublished(true);

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(mockResource));
        when(resourceRepository.save(any(Resources.class))).thenReturn(mockResource);
        ResourceResponseDto response = resourceService.unpublishResource(resourceId);
        assertNotNull(response);
        assertEquals(resourceId + "Unpublished Successfully", response.getMessage());
        assertFalse(mockResource.getPublished());

        verify(resourceRepository, times(1)).findById(resourceId);
        verify(resourceRepository, times(1)).save(mockResource);
    }


    @Test
    void testGetResourceById() {

        String resourceId = "test-resource-id";
        Resources mockResource = new Resources();
        mockResource.setId(resourceId);

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(mockResource));

        ResourceResponseDto response = resourceService.getResourceById(resourceId);

        assertNotNull(response);
        assertEquals(mockResource, response.getResourceData());

        verify(resourceRepository, times(1)).findById(resourceId);
    }

    @Test
    void testGetResourceById_NotFound() {

        String resourceId = "non-existent-resource-id";

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.empty());

        ResourcesNotFoundException exception = assertThrows(
                ResourcesNotFoundException.class,
                () -> resourceService.getResourceById(resourceId)
        );

        assertEquals("Resource with " + resourceId + " not found", exception.getMessage());
        verify(resourceRepository, times(1)).findById(resourceId);
    }


    @Test
    void testEditResources() {
        String resourceId = "test-resource-id";
        ResourceRequestDto mockRequestDto = new ResourceRequestDto();
        mockRequestDto.setId(resourceId);
        mockRequestDto.setTitle("New Title");
        mockRequestDto.setDescription("New Description");
        mockRequestDto.setStatus(false);
        mockRequestDto.setImage("new-image-url");

        Resources mockResource = new Resources();
        mockResource.setId(resourceId);
        mockResource.setTitle("Old Title");
        mockResource.setDescription("Old Description");
        mockResource.setPublished(true);
        mockResource.setImage("old-image-url");

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(mockResource));
        when(resourceRepository.save(any(Resources.class))).thenReturn(mockResource);

        ResourceResponseDto response = resourceService.editResources(mockRequestDto);

        assertNotNull(response);
        assertEquals("New Title Updated successfully", response.getMessage());
        assertEquals("New Title", mockResource.getTitle());
        assertEquals("New Description", mockResource.getDescription());
        assertFalse(mockResource.getPublished());
        assertEquals("new-image-url", mockResource.getImage());

        verify(resourceRepository, times(1)).findById(resourceId);
        verify(resourceRepository, times(1)).save(mockResource);
    }

    @Test
    void testEditResources_NotFound() {

        String resourceId = "non-existent-resource-id";
        ResourceRequestDto mockRequestDto = new ResourceRequestDto();
        mockRequestDto.setId(resourceId);

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.empty());


        ResourcesNotFoundException exception = assertThrows(
                ResourcesNotFoundException.class,
                () -> resourceService.editResources(mockRequestDto)
        );

        assertEquals("Resource with " + resourceId + " not found", exception.getMessage());

        verify(resourceRepository, times(1)).findById(resourceId);
        verify(resourceRepository, never()).save(any(Resources.class));
    }

    @Test
    void testDeleteResources() {

        String resourceId = "test-resource-id";
        Resources mockResource = new Resources();
        mockResource.setId(resourceId);

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(mockResource));

        ResourceResponseDto response = resourceService.deleteResources(resourceId);

        assertNotNull(response);
        assertEquals("Resource with " + resourceId + " Deleted successfully", response.getMessage());

        verify(resourceRepository, times(1)).findById(resourceId);
        verify(resourceRepository, times(1)).deleteById(resourceId);
    }

    @Test
    void testDeleteResources_NotFound() {

        String resourceId = "non-existent-resource-id";

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.empty());

        ResourcesNotFoundException exception = assertThrows(
                ResourcesNotFoundException.class,
                () -> resourceService.deleteResources(resourceId)
        );

        assertEquals("Resource with " + resourceId + " not found", exception.getMessage());

        verify(resourceRepository, times(1)).findById(resourceId);
        verify(resourceRepository, never()).deleteById(resourceId);
    }

    @Test
    void testAddResources() {

        ResourceRequestDto mockRequestDto = new ResourceRequestDto();
        mockRequestDto.setTitle("Test Title");
        mockRequestDto.setDescription("Test Description");
        mockRequestDto.setImage("test-image-url");
        mockRequestDto.setStatus(true);

        Resources mockResource = new Resources();
        mockResource.setTitle("Test Title");
        mockResource.setDescription("Test Description");
        mockResource.setImage("test-image-url");
        mockResource.setPublished(true);

        when(resourceRepository.save(any(Resources.class))).thenReturn(mockResource);

        ResourceResponseDto response = resourceService.addResources(mockRequestDto);

        assertNotNull(response);
        assertEquals("Test Title Created Successfully", response.getMessage());
        assertEquals(mockResource, response.getResourceData());

        verify(resourceRepository, times(1)).save(any(Resources.class));
    }

    @Test
    void testFindByTitleAndDescription() {

        String query = "Test Query";
        Pageable pageable = PageRequest.of(0, 2);

        Resources resource1 = new Resources();
        resource1.setTitle("Title 1");
        resource1.setDescription("Description 1");

        Resources resource2 = new Resources();
        resource2.setTitle("Title 2");
        resource2.setDescription("Description 2");

        List<Resources> resourceList = Arrays.asList(resource1, resource2);
        Page<Resources> mockPage = new PageImpl<>(resourceList, pageable, resourceList.size());

        when(resourceRepository.search(query, pageable)).thenReturn(mockPage);

        ResourceResponseDto response = resourceService.findByTitleAndDescriptionForUser(query, pageable);

        assertNotNull(response);
        assertEquals(2, response.getData().size());
        assertEquals(2, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());

        verify(resourceRepository, times(1)).search(query, pageable);

    }

    @Test
    void testGetAllResources() {

        Pageable pageable = PageRequest.of(0, 2);

        Resources resource1 = new Resources();
        resource1.setTitle("Title 1");
        resource1.setDescription("Description 1");

        Resources resource2 = new Resources();
        resource2.setTitle("Title 2");
        resource2.setDescription("Description 2");

        List<Resources> resourceList = Arrays.asList(resource1, resource2);
        Page<Resources> mockPage = new PageImpl<>(resourceList, pageable, resourceList.size());

        when(resourceRepository.searchAllPublishedArticles(pageable)).thenReturn(mockPage);

        ResourceResponseDto response = resourceService.getAllResources(pageable);

        assertNotNull(response);
        assertEquals(2, response.getData().size());
        assertEquals(1, response.getTotalPages());
        assertEquals(0, response.getCurrentPage());

        verify(resourceRepository, times(1)).searchAllPublishedArticles(pageable);
    }
}
