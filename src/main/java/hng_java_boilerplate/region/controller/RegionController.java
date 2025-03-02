package hng_java_boilerplate.region.controller;

import hng_java_boilerplate.exception.ErrorResponseDto;
import hng_java_boilerplate.exception.ValidationError;
import hng_java_boilerplate.region.dto.request.CreateRegion;
import hng_java_boilerplate.region.dto.request.UpdateRequest;
import hng_java_boilerplate.region.dto.response.GetAllRegion;
import hng_java_boilerplate.region.dto.response.GetResponse;
import hng_java_boilerplate.region.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/regions")
@Tag(name = "RegionController", description = "Controller for Region")
public class RegionController {
    private final RegionService regionService;

    @GetMapping
    @ApiResponse(responseCode = "200", description = "get all regions",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetAllRegion.class))
    )
    @Operation(summary = "get all regions")
    ResponseEntity<GetAllRegion> getAllRegion() {
        return ResponseEntity.ok(regionService.getAllRegions());
    }

    @GetMapping("/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get a region by user id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(responseCode = "401", description = "user not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Region not found with user id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @Operation(summary = "retrieves a user region", description = "Retrieves the region of a user using the id of the user")
    ResponseEntity<GetResponse> getRegionByUser(@PathVariable String userId) {
        return ResponseEntity.ok(regionService.getRegionByUserId(userId));
    }


    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "create a region",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "user creating a region when it already has a region created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "422", description = "Invalid or missing required request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationError.class))
            ),
    })
    @Operation(summary = "create a region", description = "Creates a region for the logged in user")
    ResponseEntity<GetResponse> createRegion(@RequestBody @Valid CreateRegion request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(regionService.createRegion(request));
    }



    @PutMapping("/{regionId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "update a region",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Region not found with id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "422", description = "Invalid or missing required request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationError.class))
            ),
    })
    @Operation(summary = "update a region", description = "update a region using the id of the region")
    ResponseEntity<GetResponse> updateRegion(@RequestBody @Valid UpdateRequest request, @PathVariable String regionId) {
        return ResponseEntity.ok(regionService.updateRegion(request, regionId));
    }


    @DeleteMapping("/{regionId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "deletes a region",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Region not found with id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @Operation(summary = "delete a region", description = "Delete a region for the logged in user")
    ResponseEntity<?> deleteRegion(@PathVariable String regionId) {
        regionService.deleteRegion(regionId);
        return ResponseEntity.noContent().build();
    }
}
