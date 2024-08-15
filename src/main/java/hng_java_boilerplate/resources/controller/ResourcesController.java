package hng_java_boilerplate.resources.controller;

import hng_java_boilerplate.resources.dto.ResourceRequestDto;
import hng_java_boilerplate.resources.dto.ResourceResponseDto;
import hng_java_boilerplate.resources.service.ResourceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resources")
public class ResourcesController {

    private final ResourceService resourceService;

    public ResourcesController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchResources(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
            if (query != null) {

                ResourceResponseDto resources = resourceService.findByTitleAndDescription(query, PageRequest.of(page, limit));
                return ResponseEntity.ok(resources);
            }
            else {

                ResourceResponseDto resources = resourceService.getAllResources(PageRequest.of(page,limit));
                return ResponseEntity.ok(resources);
            }

    }

    @PostMapping("/create")
    public ResponseEntity<?> addResources(@Valid @RequestBody ResourceRequestDto requestDto){

        ResourceResponseDto addedResources = resourceService.addResources(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedResources);
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> editResources(@Valid @RequestBody ResourceRequestDto requestDto){

        ResourceResponseDto editedResources = resourceService.editResources(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(editedResources);

    }

    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> deleteResources( @Valid @PathVariable String Id){

        ResourceResponseDto deletedResources = resourceService.deleteResources(Id);
        return ResponseEntity.status(HttpStatus.OK).body(deletedResources);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<?> getResourceById(@PathVariable String Id)
    {

            ResourceResponseDto resources = resourceService.getResourceById(Id);
            return ResponseEntity.ok(resources);

    }

    @PatchMapping("/unpublish/{id}")
    public ResponseEntity<?> unpublishResource(@Valid @PathVariable String id){

        ResourceResponseDto unpublished = resourceService.unpublishResource(id);
        return ResponseEntity.ok(unpublished);

    }

    @PatchMapping("/publish/{id}")
    public ResponseEntity<?> publishResource(@Valid @PathVariable String id){

        ResourceResponseDto unpublished = resourceService.publishResource(id);
        return ResponseEntity.ok(unpublished);

    }

}
