package hng_java_boilerplate.region.controller;
import hng_java_boilerplate.mappers.Mapper;
import hng_java_boilerplate.region.dto.*;
import hng_java_boilerplate.region.entity.UserRegionEntity;
import hng_java_boilerplate.region.service.RegionService;
import hng_java_boilerplate.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/api/v1/regions")
@Tag(name = "Regions", description = "Operations related to regions")
public class RegionController {

    private final RegionService regionService;

    private final Mapper<UserRegionEntity, UserRegionDto> userRegionMapper;

    public RegionController(RegionService regionService, Mapper<UserRegionEntity, UserRegionDto> userRegionMapper) {
        this.regionService = regionService;
        this.userRegionMapper = userRegionMapper;
    }

    @Operation(summary = "Get all regions", description = "Retrieve all available regions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation, returns a list of regions"),
            @ApiResponse(responseCode = "404", description = "Not found, no regions available")
    })
    @GetMapping()
    public ResponseEntity<?> getAllRegion(){
        Optional<?> regionList = regionService.getAllAvailableRegions();
        if (regionList.isPresent()) {
            Object responseObject = regionList.get();
            if (responseObject instanceof List<?> list && !list.isEmpty()) {

                @SuppressWarnings("unchecked")
                List<RegionDto> response = (List<RegionDto>) list;
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else  if(responseObject instanceof RegionErrorResponseDto error ){
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }else {
                return new ResponseEntity<>(
                        RegionErrorResponseDto.builder()
                                .message("Not found")
                                .statusCode("404")
                                .build(),
                        HttpStatus.NOT_FOUND
                );
            }
        }

        return new ResponseEntity<>(
                RegionErrorResponseDto.builder()
                        .message("Bad request")
                        .statusCode("400")
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @Operation(summary = "Assign a region to a user", description = "Assign a region to a user based on the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation, region assigned to the user", content = @Content(schema = @Schema(implementation = UserRegionResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found, assigned region is not available", content = @Content(schema = @Schema(implementation = RegionErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized request", content = @Content(schema = @Schema(implementation = RegionErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflict, region already assigned to the user", content = @Content(schema = @Schema(implementation = RegionErrorResponseDto.class)))
    })
    @PostMapping()
    public ResponseEntity<?> assignRegion(
            @RequestBody UserRegionDto userRegionDTO,
            Authentication authentication
    ){
        try {
        //check if region is predefined
        if(!regionService.isRegionAvailable(userRegionDTO.getRegionName())){
            return new ResponseEntity<>(
                    Optional.of(RegionErrorResponseDto.builder()
                            .statusCode("404")
                            .message("Not found")
                            .build()),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Ensure that assignee is owner of account.
        User user = (User) authentication.getPrincipal();
        String user_id = user.getId();
        if(!user_id.equals(userRegionDTO.getUserId())){
            return new ResponseEntity<>(
                    Optional.of(RegionErrorResponseDto.builder()
                            .statusCode("401")
                            .message("Unauthorized request")
                            .build()),
                    HttpStatus.UNAUTHORIZED
            );
        }
            if(regionService.hasUserAssignedRegion(userRegionDTO.getUserId())){
                return new ResponseEntity<>(
                        Optional.of(RegionErrorResponseDto.builder()
                                .statusCode("409")
                                .message("Conflict")
                                .build()),
                        HttpStatus.CONFLICT
                );
            }
            UserRegionEntity userRegionEntity = userRegionMapper.mapFrom(userRegionDTO);
            userRegionEntity.setRegionCode(userRegionDTO.getCountryCode());
            Optional<?> assignedRegion = regionService.saveUserRegion(userRegionEntity);
            UserRegionDto region = userRegionMapper.mapTo((UserRegionEntity) assignedRegion.get());
            return new ResponseEntity<>(
                    UserRegionResponseDto.builder()
                            .regionName(region.getRegionName())
                            .countryCode(region.getCountryCode())
                            .userId(region.getUserId())
                            .build()
                    , HttpStatus.OK);
        } catch (Exception e) {
            log.error("========= Error message =========> {}", e.getMessage());
            return new ResponseEntity<>(
                    Optional.of(RegionErrorResponseDto.builder()
                            .statusCode("400")
                            .message("Bad request")
                            .build()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Operation(summary = "Get user's region", description = "Retrieve the region assigned to a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Successful operation, returns the user's region", content = @Content(schema = @Schema(implementation = UserRegionDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid parameters", content = @Content(schema = @Schema(implementation = RegionErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found, no region assigned to the user", content = @Content(schema = @Schema(implementation = RegionErrorResponseDto.class)))
    })
    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserRegion(@PathVariable("user_id") String userID){
        try {
            UUID userId = UUID.fromString(userID);
            Optional<UserRegionEntity> userRegion = regionService.getUserRegion(userId);
            if(userRegion.isPresent()){
                UserRegionDto userRegionDto = userRegionMapper.mapTo(userRegion.get());
                return new ResponseEntity<>(userRegionDto, HttpStatus.FOUND);
            }
            return new ResponseEntity<>(
                    RegionErrorResponseDto.builder()
                            .statusCode("404")
                            .message("Not found")
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Optional.of(RegionErrorResponseDto.builder()
                            .statusCode("400")
                            .message("Bad request")
                            .build()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Operation(summary = "Update user's region", description = "Update the region assigned to a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation, returns the updated region", content = @Content(schema = @Schema(implementation = UpdateUserRegionDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, region chosen is not an assigned region", content = @Content(schema = @Schema(implementation = RegionErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized request", content = @Content(schema = @Schema(implementation = RegionErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found, no region assigned to the user", content = @Content(schema = @Schema(implementation = RegionErrorResponseDto.class)))
    })
    @PutMapping("/{user_id}")
    public ResponseEntity<?> updateUserRegion(
            @PathVariable("user_id") String userID,
            @RequestBody RegionUpdateDto regionUpdateDto,
            Authentication authentication
    ) {
// Ensure that only the current authenticated users can update their region preferences.
        User user = (User) authentication.getPrincipal();
        String user_id = user.getId();
        if(!user_id.equals(userID)){
            return new ResponseEntity<>(
                    Optional.of(RegionErrorResponseDto.builder()
                            .statusCode("401")
                            .message("Unauthorized request")
                            .build()),
                    HttpStatus.UNAUTHORIZED
            );
        }

        try {
            if(!regionService.isRegionAvailable(regionUpdateDto.getRegionName())){
                return new ResponseEntity<>(
                        Optional.of(RegionErrorResponseDto.builder()
                                .statusCode("400")
                                .message("Bad request")
                                .build()),
                        HttpStatus.BAD_REQUEST
                );
            }
            UUID userId = UUID.fromString(userID);
            Optional<UserRegionEntity> updatedUserRegion = regionService.updateUserRegion(userId, regionUpdateDto);
            if(updatedUserRegion.isPresent()){
                UserRegionDto userRegionDto = userRegionMapper.mapTo(updatedUserRegion.get());
                return new ResponseEntity<>(
                        UpdateUserRegionDto.builder()
                                .countryCode(userRegionDto.getCountryCode())
                                .regionName(userRegionDto.getRegionName())
                                .build()
                        , HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    Optional.of(RegionErrorResponseDto.builder()
                            .statusCode("404")
                            .message("Not found")
                            .build()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(
                    Optional.of(RegionErrorResponseDto.builder()
                            .statusCode("400")
                            .message("Bad request")
                            .build()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
