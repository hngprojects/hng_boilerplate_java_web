package hng_java_boilerplate.unitTests.services;


import hng_java_boilerplate.region.dto.RegionDto;
import hng_java_boilerplate.region.dto.RegionErrorResponseDto;
import hng_java_boilerplate.region.dto.RegionUpdateDto;
import hng_java_boilerplate.region.entity.RegionEntity;
import hng_java_boilerplate.region.entity.UserRegionEntity;
import hng_java_boilerplate.region.repository.RegionRepository;
import hng_java_boilerplate.region.repository.UserRegionRepository;
import hng_java_boilerplate.region.service.RegionService;
import hng_java_boilerplate.utils.TestDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RegionServiceUnitTest {


    @Mock
    private RegionRepository regionRepository;

    @Mock
    private UserRegionRepository userRegionRepository;

    @InjectMocks
    private RegionService underTest;


    @Test
    public void test_that_get_all_region_returns_regions(){

        RegionEntity regionEntity = TestDataUtils.createRegionEntity();
        RegionDto regionDto = TestDataUtils.createRegionDto();

        List<RegionEntity> regionList = List.of(regionEntity);

        when(regionRepository.findAll()).thenReturn(regionList);
            Optional<?> regions = underTest.getAllAvailableRegions();
            assertThat(regions).isPresent();
            assertThat(regions.get()).isEqualTo(List.of(regionDto));
    }


    @Test
    public void test_that_get_all_region_returns_error(){

        RegionDto regionDto = TestDataUtils.createRegionDto();
        RegionErrorResponseDto error = TestDataUtils.createBadRequestError();

        when(regionRepository.findAll()).thenThrow(new RuntimeException("Database error"));
        Optional<?> regions = underTest.getAllAvailableRegions();

        assertThat(regions).isPresent();
        assertThat(regions.get()).isEqualTo(error);
    }

    @Test
    public void test_that_save_user_region_saves_and_return_region(){
        UserRegionEntity userRegion = TestDataUtils.createUserRegionEntity();

         when(userRegionRepository.save(userRegion)).thenReturn(userRegion);
        Optional<?> savedRegion = underTest.saveUserRegion(userRegion);

        assertThat(savedRegion).isPresent();
        assertThat(savedRegion.get()).isEqualTo(userRegion);
    }

    @Test
    public void test_that_updateUserRegion_updates_and_return_region_if_user_assigned_region(){
        UserRegionEntity userRegion = TestDataUtils.createUserRegionEntity();
        UUID userId = UUID.fromString("8f681c4a-a888-48b0-be54-51d7a2c2329c");
        RegionUpdateDto regionUpdateDto = RegionUpdateDto.builder()
                .regionName("Asia")
                .countryCode("AS")
                .build();

        UserRegionEntity updatedUserRegion = UserRegionEntity.builder()
                .regionId(1)
                .userId(UUID.fromString("8f681c4a-a888-48b0-be54-51d7a2c2329c"))
                .regionName("Asia")
                .regionCode("AS")
                .build();

        when(userRegionRepository.findByUserId(userId)).thenReturn(Optional.of(userRegion));

        Optional<UserRegionEntity> updated = underTest.updateUserRegion(userId, regionUpdateDto);
        assertThat(updated).isPresent();
        assertThat(updated.get()).isEqualTo(updatedUserRegion);
    }


    @Test
    public void test_that_updateUserRegion_returns_optional_empty_if_no_assigned_region(){
        UUID userId = UUID.fromString("8f681c4a-a888-48b0-be54-51d7a2c2329c");
        RegionUpdateDto regionUpdateDto = RegionUpdateDto.builder()
                .regionName("Asia")
                .countryCode("AS")
                .build();

        when(userRegionRepository.findByUserId(userId)).thenReturn(Optional.empty());

        Optional<UserRegionEntity> updated = underTest.updateUserRegion(userId, regionUpdateDto);
        assertThat(updated).isEmpty();
    }

    @Test
    public void test_that_isRegionAvailable_returns_true_when_region_available(){
        String regionName = "North America";
        RegionEntity regionEntity = TestDataUtils.createRegionEntity();

        when(regionRepository.findAll()).thenReturn(List.of(regionEntity));

        Boolean isAvailable = underTest.isRegionAvailable(regionName);
        assertThat(isAvailable).isTrue();
    }

    @Test
    public void test_that_isRegionAvailable_returns_false_when_region_is_not_available(){
        String regionName = "South America";
        RegionEntity regionEntity = TestDataUtils.createRegionEntity();

        when(regionRepository.findAll()).thenReturn(List.of(regionEntity));

        Boolean isAvailable = underTest.isRegionAvailable(regionName);
        assertThat(isAvailable).isFalse();
    }

    @Test
    public void test_that_isRegionAvailable_returns_false_if_error_occur(){
        String regionName = "South America";

        when(regionRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        Boolean isAvailable = underTest.isRegionAvailable(regionName);
        assertThat(isAvailable).isFalse();
    }

}
