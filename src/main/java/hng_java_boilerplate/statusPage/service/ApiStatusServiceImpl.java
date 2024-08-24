package hng_java_boilerplate.statusPage.service;

import hng_java_boilerplate.statusPage.entity.ApiStatus;
import hng_java_boilerplate.statusPage.repository.ApiStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiStatusServiceImpl implements ApiStatusService {

    private final ApiStatusRepository apiStatusRepository;

    public List<ApiStatus> getAllApiStatuses() {
        return apiStatusRepository.findAll();
    }

    public ApiStatus updateApiStatus(ApiStatus apiStatus) {
        ApiStatus existingStatus = apiStatusRepository.findByApiGroup(apiStatus.getApiGroup());
        if (existingStatus != null) {
            existingStatus.setStatus(apiStatus.getStatus());
            existingStatus.setLastChecked(apiStatus.getLastChecked());
            existingStatus.setResponseTime(apiStatus.getResponseTime());
            existingStatus.setDetails(apiStatus.getDetails());
            return apiStatusRepository.save(existingStatus);
        }
        return apiStatusRepository.save(apiStatus);
    }
}