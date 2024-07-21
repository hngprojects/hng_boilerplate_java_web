package hng_java_boilerplate.utils;

import hng_java_boilerplate.region.dto.RegionDto;
import hng_java_boilerplate.region.dto.RegionErrorResponseDto;
import hng_java_boilerplate.region.dto.UserRegionDto;
import hng_java_boilerplate.region.entity.RegionEntity;
import hng_java_boilerplate.region.entity.UserRegionEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public final class TestDataUtils {

    public static RegionEntity createRegionEntity(){
        return RegionEntity.builder()
                .regionCode("region-001")
                .regionName("North America")
                .status(1)
                .createdOn(LocalDateTime.of(2024, 7, 19, 10, 0))
                .createdBy("system")
                .modifiedOn(LocalDateTime.of(2024, 7, 19, 10, 0))
                .modifiedBy("system")
                .build();
    }

    public static RegionEntity createRegionEntity2(){
        return RegionEntity.builder()
                .regionCode("region-001")
                .regionName("Africa")
                .status(1)
                .createdOn(LocalDateTime.of(2024, 7, 19, 10, 0))
                .createdBy("system")
                .modifiedOn(LocalDateTime.of(2024, 7, 19, 10, 0))
                .modifiedBy("system")
                .build();
    }

    public static RegionDto createRegionDto(){
        return RegionDto.builder()
                .regionCode("region-001")
                .regionName("North America")
                .status(1)
                .createdOn(LocalDateTime.of(2024, 7, 19, 10, 0))
                .createdBy("system")
                .modifiedOn(LocalDateTime.of(2024, 7, 19, 10, 0))
                .modifiedBy("system")
                .build();
    }

    public static RegionErrorResponseDto createBadRequestError(){
        return RegionErrorResponseDto.builder()
                .statusCode("400")
                .message("Bad request")
                .build();
    }

    public static UserRegionEntity createUserRegionEntity(){
        return UserRegionEntity.builder()
                .regionId(1)
                .userId(UUID.fromString("8f681c4a-a888-48b0-be54-51d7a2c2329c"))
                .regionName("North America")
                .regionCode("NA")
                .build();
    }

    public static UserRegionDto createUserRegionDto(){
        return UserRegionDto.builder()
                .regionId(1)
                .userId(UUID.fromString("8f681c4a-a888-48b0-be54-51d7a2c2329c"))
                .regionName("North America")
                .countryCode("NA")
                .build();
    }
}
