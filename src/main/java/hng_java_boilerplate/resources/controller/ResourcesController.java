package hng_java_boilerplate.resources.controller;

import hng_java_boilerplate.resources.dto.ResourceRequestDto;
import hng_java_boilerplate.resources.dto.ResourceResponseDto;
import hng_java_boilerplate.resources.service.ResourceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resources")
public class ResourcesController {

    private final ResourceService resourceService;

    public ResourcesController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/articles")
    public ResponseEntity<?> getAllResourcesForUsersOnly(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int limit)
    {
            ResourceResponseDto resources = resourceService.getAllResources(PageRequest.of(page,limit));
            return ResponseEntity.ok(resources);
    }

    @GetMapping("/searchResources")
    public ResponseEntity<?> searchResourcesForUsersOnly(
            @RequestParam() String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int limit)
    {
        ResourceResponseDto resources = resourceService.findByTitleAndDescriptionForUser(query, PageRequest.of(page, limit));
        return ResponseEntity.ok(resources);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<?> searchResourcesForAdminOnly( @RequestParam String query)
    {
        ResourceResponseDto resources = resourceService.findByTitleAndDescriptionForAdmin(query);
        return ResponseEntity.ok(resources);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> addResources(@Valid @RequestBody ResourceRequestDto requestDto)
    {
        ResourceResponseDto addedResources = resourceService.addResources(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedResources);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PatchMapping("/edit")
    public ResponseEntity<?> editResources(@Valid @RequestBody ResourceRequestDto requestDto)
    {
        ResourceResponseDto editedResources = resourceService.editResources(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(editedResources);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> deleteResources( @Valid @PathVariable String Id)
    {
        ResourceResponseDto deletedResources = resourceService.deleteResources(Id);
        return ResponseEntity.status(HttpStatus.OK).body(deletedResources);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/{Id}")
    public ResponseEntity<?> getResourcesById(@PathVariable String Id)
    {
            ResourceResponseDto resources = resourceService.getResourceById(Id);
            return ResponseEntity.ok(resources);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PatchMapping("/unpublish/{id}")
    public ResponseEntity<?> unpublishResource(@Valid @PathVariable String id)
    {
        ResourceResponseDto unpublished = resourceService.unpublishResource(id);
        return ResponseEntity.ok(unpublished);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PatchMapping("/publish/{id}")
    public ResponseEntity<?> publishResource(@Valid @PathVariable String id)
    {
        ResourceResponseDto unpublished = resourceService.publishResource(id);
        return ResponseEntity.ok(unpublished);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/published")
    public ResponseEntity<?> getAllPublishedResourceBy()
    {
        ResourceResponseDto resources = resourceService.getAllPublishedResource();
        return ResponseEntity.ok(resources);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/unpublished")
    public ResponseEntity<?> getAllUnpublishedResourceBy()
    {
        ResourceResponseDto resources = resourceService.getAllUnPublishedResource();
        return ResponseEntity.ok(resources);
    }

}
