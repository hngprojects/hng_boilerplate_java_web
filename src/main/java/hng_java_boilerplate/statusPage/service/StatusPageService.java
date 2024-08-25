package hng_java_boilerplate.statusPage.service;

import com.fasterxml.jackson.databind.JsonNode;
import hng_java_boilerplate.statusPage.entity.StatusPage;

import java.util.List;

public interface StatusPageService {
    void updateApiStatus();
    void processJsonData(JsonNode jsonNode);
    List<StatusPage> getAllApiStatuses();
}
