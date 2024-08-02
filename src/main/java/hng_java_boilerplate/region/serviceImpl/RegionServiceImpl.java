package hng_java_boilerplate.region.serviceImpl;

import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.region.dto.request.CreateRegion;
import hng_java_boilerplate.region.dto.request.UpdateRequest;
import hng_java_boilerplate.region.dto.response.GetAllRegion;
import hng_java_boilerplate.region.dto.response.GetResponse;
import hng_java_boilerplate.region.entity.Region;
import hng_java_boilerplate.region.repository.RegionRepository;
import hng_java_boilerplate.region.service.RegionService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {
    private final RegionRepository regionRepository;
    private final UserService userService;

    @Override
    @Transactional
    public GetResponse getRegionByUserId(String userId) {
        Region region = regionRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("region not found for user"));

        return GetResponse.builder()
                .id(region.getId())
                .user_id(region.getUser().getId())
                .region(region.getRegion())
                .language(region.getLanguage())
                .timezone(region.getTimezone())
                .build();
    }

    @Override
    public GetResponse createRegion(CreateRegion request) {
        User user = userService.getLoggedInUser();

        Optional<Region> region = regionRepository.findByUserId(user.getId());
        if (region.isPresent()) {
            throw new BadRequestException("user cannot have more than one region at a time");
        }


        Region newRegion = new Region();
        newRegion.setRegion(request.getRegion());
        newRegion.setLanguage(request.getLanguage());
        newRegion.setTimezone(request.getTimezone());
        newRegion.setUser(user);

        regionRepository.saveAndFlush(newRegion);

        return GetResponse.builder()
                .id(newRegion.getId())
                .user_id(newRegion.getUser().getId())
                .region(newRegion.getRegion())
                .language(newRegion.getLanguage())
                .timezone((newRegion.getTimezone()))
                .build();
    }

    @Override
    public GetAllRegion getAllRegions() {
        List<Region> regions = regionRepository.findAll();

        List<GetResponse> data = regions.stream().map((region) ->
                GetResponse.builder()
                        .id(region.getId())
                        .user_id(region.getUser().getId())
                        .timezone(region.getTimezone())
                        .language(region.getLanguage())
                        .region(region.getRegion())
                        .build()).toList();

        return new GetAllRegion(200, "All Regions", data);
    }

    @Override
    public GetResponse updateRegion(UpdateRequest request, String id) {
        Region region = getRegion(id);

        region.setLanguage(request.getLanguage());
        region.setRegion(request.getRegion());
        region.setTimezone(request.getTimezone());

        regionRepository.saveAndFlush(region);

        return GetResponse.builder()
                .id(region.getId())
                .user_id(region.getUser().getId())
                .region(region.getRegion())
                .language(region.getLanguage())
                .timezone(region.getTimezone())
                .build();
    }

    @Override
    public void deleteRegion(String regionId) {
        Region region = getRegion(regionId);
        regionRepository.delete(region);
    }

    private Region getRegion(String regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new NotFoundException("region not found"));
    }
}
