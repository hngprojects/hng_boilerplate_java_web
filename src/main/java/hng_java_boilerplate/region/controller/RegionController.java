package hng_java_boilerplate.region.controller;

import hng_java_boilerplate.mappers.Mapper;
import hng_java_boilerplate.region.dto.*;
import hng_java_boilerplate.region.entity.UserRegionEntity;
import hng_java_boilerplate.region.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.security.core.Authentication;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/regions")
public class RegionController {

    private final RegionService regionService;

    private final Mapper<UserRegionEntity, UserRegionDto> userRegionMapper;

    @Autowired
    public RegionController(RegionService regionService, Mapper<UserRegionEntity, UserRegionDto> userRegionMapper) {
        this.regionService = regionService;
        this.userRegionMapper = userRegionMapper;
    }


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

    @PostMapping()
    public ResponseEntity<?> assignRegion(
            @RequestBody UserRegionDto userRegionDTO
    ){

        try {
        //check if region is predefined
        if(!regionService.isRegionAvailable(userRegionDTO.getRegionName())){
            return new ResponseEntity<>(
                    Optional.of(RegionErrorResponseDto.builder()
                            .statusCode("400")
                            .message("Bad request")
                            .build()),
                    HttpStatus.BAD_REQUEST
            );
        }


        //========== Ensure that assignee is owner of account. =================
//        UserEntity user = (UserEntity) authentication.getPrincipal();
//        String user_id = user.getUserId();
//        if(!user_id.equals(userId)){
//            return new ResponseEntity<>(
//                    Optional.of(RegionErrorResponseDto.builder()
//                            .statusCode("401")
//                            .message("Unauthorized request")
//                            .build()),
//                    HttpStatus.UNAUTHORIZED
//            );
//        }




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
            System.out.println("========= Error message =========> " + e.getMessage());
            return new ResponseEntity<>(
                    Optional.of(RegionErrorResponseDto.builder()
                            .statusCode("400")
                            .message("Bad request")
                            .build()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

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

    @PutMapping("/{user_id}")
    public ResponseEntity<?> updateUserRegion(
            @PathVariable("user_id") String userID,
            @RequestBody RegionUpdateDto regionUpdateDto
    ) {



//========== Ensure that only authenticated users can update their region preferences. ===============================================
//        UserEntity user = (UserEntity) authentication.getPrincipal();
//
//        String user_id = user.getUserId();
//        if(!user_id.equals(userId)){
//            return new ResponseEntity<>(
//                    Optional.of(RegionErrorResponseDto.builder()
//                            .statusCode("401")
//                            .message("Unauthorized request")
//                            .build()),
//                    HttpStatus.UNAUTHORIZED
//            );
//        }

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
                            .statusCode("400")
                            .message("Bad request")
                            .build()),
                    HttpStatus.BAD_REQUEST
            );

        } catch (Exception e) {
            System.out.println("====== Error message =====> " + e.getMessage());
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
