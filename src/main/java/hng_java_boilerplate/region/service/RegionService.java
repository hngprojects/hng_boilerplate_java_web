package hng_java_boilerplate.region.service;

import hng_java_boilerplate.region.dto.request.CreateRegion;
import hng_java_boilerplate.region.dto.request.UpdateRequest;
import hng_java_boilerplate.region.dto.response.GetAllRegion;
import hng_java_boilerplate.region.dto.response.GetResponse;

public interface RegionService {
    GetResponse getRegionByUserId(String userId);
    GetResponse createRegion(CreateRegion request);
    GetAllRegion getAllRegions();
    GetResponse updateRegion(UpdateRequest request, String id);
    void deleteRegion(String regionId);
}
