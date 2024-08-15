package hng_java_boilerplate.resources.service;

import hng_java_boilerplate.resources.dto.ResourceRequestDto;
import hng_java_boilerplate.resources.dto.ResourceResponseDto;
import org.springframework.data.domain.Pageable;

public interface ResourceService {

    ResourceResponseDto findByTitleAndDescriptionForUser(String query, Pageable pageable);
    ResourceResponseDto getAllResources(Pageable pageable);
    ResourceResponseDto addResources(ResourceRequestDto resourceRequestDto);
    ResourceResponseDto deleteResources(String Id);
    ResourceResponseDto editResources(ResourceRequestDto resourceRequestDto);
    ResourceResponseDto getResourceById(String Id);
    ResourceResponseDto unpublishResource(String Id);
    ResourceResponseDto publishResource(String Id);
    ResourceResponseDto getAllPublishedResource();
    ResourceResponseDto findByTitleAndDescriptionForAdmin(String query);
    ResourceResponseDto getAllUnPublishedResource();

}
