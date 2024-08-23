package hng_java_boilerplate.statusPage.service;

import hng_java_boilerplate.statusPage.entity.ApiStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApiStatusService {
    List<ApiStatus> getAllApiStatuses();
    ApiStatus updateApiStatus(ApiStatus apiStatus);
}