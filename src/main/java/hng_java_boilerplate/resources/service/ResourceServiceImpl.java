package hng_java_boilerplate.resources.service;
import hng_java_boilerplate.resources.dto.ResourceRequestDto;
import hng_java_boilerplate.resources.dto.ResourceResponseDto;
import hng_java_boilerplate.resources.entity.Resources;
import hng_java_boilerplate.resources.exception.ResourcesNotFoundException;
import hng_java_boilerplate.resources.repository.ResourceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService{

   private final ResourceRepository resourceRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    public ResourceResponseDto findByTitleAndDescriptionForUser(String query, Pageable pageable) {
        Page<Resources> resources = resourceRepository.search(query,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));

        ResourceResponseDto responseDto = new ResourceResponseDto();
        responseDto.setData(resources.getContent());
        responseDto.setTotalPages(resources.getSize());
        responseDto.setCurrentPage(resources.getNumber());
        return responseDto;
    }

    @Override
    public ResourceResponseDto findByTitleAndDescriptionForAdmin(String query) {
        List<Resources> resources = resourceRepository.searchAllResourcesForAdmin(query);
        if (resources.isEmpty()) {
            throw new ResourcesNotFoundException("No resources found with query: " + query);
        }
        ResourceResponseDto responseDto = new ResourceResponseDto();
        responseDto.setData(resources);
        return responseDto;
    }

    @Override
    public ResourceResponseDto getAllResources(Pageable pageable) {
        Page<Resources> resources = resourceRepository.searchAllPublishedArticles(pageable);

        ResourceResponseDto responseDto = new ResourceResponseDto();
        responseDto.setData(resources.getContent());
        responseDto.setCurrentPage(resources.getNumber());
        responseDto.setTotalPages(resources.getTotalPages());
        return responseDto;
    }

    @Override
    public ResourceResponseDto addResources(ResourceRequestDto resourceRequestDto) {
        Resources resources =new Resources();
        resources.setTitle(resourceRequestDto.getTitle());
        resources.setDescription(resourceRequestDto.getDescription());
        resources.setImage(resourceRequestDto.getImage());
        resources.setPublished(resourceRequestDto.getStatus());
        resourceRepository.save(resources);

        ResourceResponseDto responseDto = new ResourceResponseDto();
        responseDto.setMessage(resources.getTitle() + " Created Successfully");
        responseDto.setResourceData(resources);
        return responseDto;
    }

    @Override
    public ResourceResponseDto deleteResources(String Id) {

        Resources resources = resourceRepository.findById(Id).stream()
                .filter(foundResource -> foundResource.getId().equals(Id))
                .findFirst()
                .orElseThrow(() -> new ResourcesNotFoundException("Resource with " +Id+ " not found"));

        resourceRepository.deleteById(resources.getId());
        ResourceResponseDto responseDto = new ResourceResponseDto();
        responseDto.setMessage("Resource with " +Id+ " Deleted successfully");

        return responseDto;
    }

    @Override
    public ResourceResponseDto editResources(ResourceRequestDto resourceRequestDto) {
        Resources resources = resourceRepository.findById(resourceRequestDto.getId()).stream()
                .filter(foundResource -> foundResource.getId().equals(resourceRequestDto.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourcesNotFoundException("Resource with " +resourceRequestDto.getId()+ " not found"));


        if (resourceRequestDto.getId() != null){
            resources.setId(resourceRequestDto.getId());
        }
        if (resourceRequestDto.getTitle() != null){
            resources.setTitle(resourceRequestDto.getTitle());
        }
        if (resourceRequestDto.getDescription() != null){
            resources.setDescription(resourceRequestDto.getDescription());
        }
        if (resourceRequestDto.getImage() != null ){
            resources.setPublished(resourceRequestDto.getStatus());
        }
        if ( resourceRequestDto.getImage() != null ){
            resources.setImage(resourceRequestDto.getImage());
        }
        resourceRepository.save(resources);
        ResourceResponseDto responseDto = new ResourceResponseDto();
        responseDto.setMessage(resources.getTitle() + " Updated successfully");
        responseDto.setResourceData(resources);

        return responseDto;

    }

    @Override
    public ResourceResponseDto getResourceById(String Id) {
        Resources resources = resourceRepository.findById(Id).stream()
                .filter(foundResource -> foundResource.getId().equals(Id))
                .findFirst()
                .orElseThrow(() -> new ResourcesNotFoundException("Resource with " +Id+ " not found"));

        ResourceResponseDto responseDto = new ResourceResponseDto();
        responseDto.setResourceData(resources);
        return responseDto;
    }

    @Override
    public ResourceResponseDto unpublishResource(String Id) {
        Resources resources = resourceRepository.findById(Id).stream()
                .filter(foundResource -> foundResource.getId().equals(Id))
                .findFirst()
                .orElseThrow(() -> new ResourcesNotFoundException("Resource with " +Id+ "not found"));

        resources.setPublished(false);
        resourceRepository.save(resources);

        ResourceResponseDto responseDto = new ResourceResponseDto();
        responseDto.setMessage(Id + "Unpublished Successfully");
        responseDto.setResourceData(resources);

        return responseDto;
    }

    @Override
    public ResourceResponseDto publishResource(String Id) {
        Resources resources = resourceRepository.findById(Id).stream()
                .filter(foundResource -> foundResource.getId().equals(Id))
                .findFirst()
                .orElseThrow(() -> new ResourcesNotFoundException("Resource with " +Id+ "not found"));

        resources.setPublished(true);
        resourceRepository.save(resources);

        ResourceResponseDto responseDto = new ResourceResponseDto();
        responseDto.setMessage(Id + " published Successfully");
        responseDto.setResourceData(resources);

        return responseDto;
    }

    @Override
    public ResourceResponseDto getAllPublishedResource() {
        List<Resources> resources = resourceRepository.getAllPublishedResources();
        if (resources.isEmpty()) {
            throw new ResourcesNotFoundException("No resources found");
        }
        ResourceResponseDto responseDto = new ResourceResponseDto();
        responseDto.setData(resources);

        return responseDto;
    }

    @Override
    public ResourceResponseDto getAllUnPublishedResource() {
        List<Resources> resources = resourceRepository.getAllUnPublishedResources();
        if (resources.isEmpty()) {
            throw new ResourcesNotFoundException("No resources found");
        }
        ResourceResponseDto responseDto = new ResourceResponseDto();
        responseDto.setData(resources);

        return responseDto;
    }
}
