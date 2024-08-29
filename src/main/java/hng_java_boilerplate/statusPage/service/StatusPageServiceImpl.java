package hng_java_boilerplate.statusPage.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.statusPage.entity.StatusPage;
import hng_java_boilerplate.statusPage.entity.StatusPage.Status;
import hng_java_boilerplate.statusPage.repository.StatusPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusPageServiceImpl implements StatusPageService {

    private final StatusPageRepository statusPageRepository;
    private final ObjectMapper objectMapper;
    private static final String RESULT_JSON_PATH = "/home/${{ secrets.USERNAME }}/hng_boilerplate_java_web/result.json";

    @Override
    @Scheduled(fixedRate = 900000)
    public void updateApiStatus() {
        try {
            File jsonFile = new File(RESULT_JSON_PATH);
            JsonNode jsonNode = objectMapper.readTree(jsonFile);
            processJsonData(jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processJsonData(JsonNode jsonNode) {
        JsonNode executions = jsonNode.get("run").get("executions");

        for (JsonNode execution : executions) {
            String apiGroup = execution.get("item").get("name").asText();
            int responseTime = execution.get("response").get("responseTime").asInt();
            int statusCode = execution.get("response").get("code").asInt();
            String statusMessage = execution.get("response").get("status").asText();

            StatusPage statusPage = new StatusPage();
            statusPage.setApiGroup(apiGroup);
            statusPage.setResponseTime(responseTime);
            statusPage.setLastChecked(LocalDateTime.now());

            if (statusCode >= 200 && statusCode < 300) {
                statusPage.setStatus(Status.OPERATIONAL);
            } else if (statusCode >= 500) {
                statusPage.setStatus(Status.DEGRADED);
            } else {
                statusPage.setStatus(Status.DOWN);
            }

            statusPage.setDetails(statusMessage);

            statusPageRepository.save(statusPage);
        }
    }

    @Override
    public List<StatusPage> getAllApiStatuses() {
        return statusPageRepository.findAll();
    }
}
