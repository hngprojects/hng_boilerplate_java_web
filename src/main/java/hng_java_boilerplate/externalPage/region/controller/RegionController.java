package hng_java_boilerplate.externalPage.region.controller;

import hng_java_boilerplate.externalPage.region.service.RegionService;
import hng_java_boilerplate.externalPage.region.dto.request.CreateRegion;
import hng_java_boilerplate.externalPage.region.dto.request.UpdateRequest;
import hng_java_boilerplate.externalPage.region.dto.response.GetAllRegion;
import hng_java_boilerplate.externalPage.region.dto.response.GetResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/regions")
public class RegionController {
    private final RegionService regionService;

    @GetMapping
    ResponseEntity<GetAllRegion> getAllRegion() {
        return ResponseEntity.ok(regionService.getAllRegions());
    }

    @GetMapping("/{user_id}")
    ResponseEntity<GetResponse> getRegionByUser(@PathVariable String user_id) {
        return ResponseEntity.ok(regionService.getRegionByUserId(user_id));
    }

    @PostMapping
    ResponseEntity<GetResponse> createRegion(@RequestBody @Valid CreateRegion request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(regionService.createRegion(request));
    }

    @PutMapping("/{region_id}")
    ResponseEntity<GetResponse> updateRegion(@RequestBody @Valid UpdateRequest request, @PathVariable String region_id) {
        return ResponseEntity.ok(regionService.updateRegion(request, region_id));
    }

    @DeleteMapping("/{region_id}")
    ResponseEntity<?> deleteRegion(@PathVariable String region_id) {
        regionService.deleteRegion(region_id);
        return ResponseEntity.noContent().build();
    }
}
