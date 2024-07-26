package hng_java_boilerplate.region.service;
import hng_java_boilerplate.region.dto.RegionDto;
import hng_java_boilerplate.region.dto.RegionErrorResponseDto;
import hng_java_boilerplate.region.dto.RegionUpdateDto;
import hng_java_boilerplate.region.entity.RegionEntity;
import hng_java_boilerplate.region.entity.UserRegionEntity;
import hng_java_boilerplate.region.repository.RegionRepository;
import hng_java_boilerplate.region.repository.UserRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegionService {

    private final RegionRepository regionRepository;

    private final UserRegionRepository userRegionRepository;

    public RegionService(RegionRepository regionRepository, UserRegionRepository userRegionRepository) {
        this.regionRepository = regionRepository;
        this.userRegionRepository = userRegionRepository;
    }

    public Optional<Object> getAllAvailableRegions() {
        try {
            List<RegionEntity> allRegions = regionRepository.findAll();
            List<RegionDto> regionDTOs = allRegions.stream()
                    .map(regionEntity -> new RegionDto(
                            regionEntity.getRegionCode(),
                            regionEntity.getRegionName(),
                            regionEntity.getStatus(),
                            regionEntity.getCreatedOn(),
                            regionEntity.getCreatedBy(),
                            regionEntity.getModifiedOn(),
                            regionEntity.getModifiedBy()
                    ))
                    .collect(Collectors.toList());
            return Optional.of(regionDTOs);
        } catch (Exception e) {
            return Optional.of(RegionErrorResponseDto.builder()
                    .statusCode("400")
                    .message("Bad request")
                    .build());
        }
    }

    public Optional<?> saveUserRegion(UserRegionEntity userRegionEntity) {
            UserRegionEntity savedUserRegion = userRegionRepository.save(userRegionEntity);
            return Optional.of(savedUserRegion);
    }

    public Optional<UserRegionEntity> getUserRegion(UUID userId) {
        return userRegionRepository.findByUserId(userId);
    }

    public Optional<UserRegionEntity> updateUserRegion(
            UUID userId, RegionUpdateDto regionUpdateDto
    ) {
        Optional<UserRegionEntity> userRegion = userRegionRepository.findByUserId(userId);
        if (userRegion.isPresent()) {
            UserRegionEntity existingUserRegion = userRegion.get();
            existingUserRegion.setRegionName(regionUpdateDto.getRegionName());
            existingUserRegion.setRegionCode(regionUpdateDto.getCountryCode());
            userRegionRepository.save(existingUserRegion);
            return Optional.of(existingUserRegion);
        } else {
            return Optional.empty();
        }
    }

    public Optional<RegionEntity> createRegion(RegionEntity regionEntity){
        return Optional.of(regionRepository.save(regionEntity));
    }

    public boolean isRegionAvailable(String regionName) {
        try {
            List<RegionEntity> allRegions = regionRepository.findAll();
            List<RegionDto> availableRegions = allRegions.stream()
                    .map(regionEntity -> new RegionDto(
                            regionEntity.getRegionCode(),
                            regionEntity.getRegionName(),
                            regionEntity.getStatus(),
                            regionEntity.getCreatedOn(),
                            regionEntity.getCreatedBy(),
                            regionEntity.getModifiedOn(),
                            regionEntity.getModifiedBy()
                    ))
                    .toList();

            for (RegionDto region: availableRegions){
                if(region.getRegionName().equals(regionName)){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasUserAssignedRegion(UUID userId) {
        Optional<UserRegionEntity> userRegion = userRegionRepository.findByUserId(userId);
        return userRegion.isPresent();
    }
}
