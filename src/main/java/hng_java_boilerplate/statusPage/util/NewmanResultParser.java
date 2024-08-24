package hng_java_boilerplate.statusPage.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.statusPage.entity.ApiStatus;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NewmanResultParser {

    public static List<ApiStatus> parseNewmanResults(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(filePath));

        List<ApiStatus> apiStatuses = new ArrayList<>();

        JsonNode executions = rootNode.path("run").path("executions");
        for (JsonNode execution : executions) {
            String apiGroup = execution.path("item").path("name").asText();
            boolean passed = execution.path("response").path("code").asInt() == 200;
            int responseTime = execution.path("response").path("responseTime").asInt();

            ApiStatus apiStatus = new ApiStatus();
            apiStatus.setApiGroup(apiGroup);
            apiStatus.setStatus(passed ? ApiStatus.Status.OPERATIONAL : ApiStatus.Status.DOWN);
            apiStatus.setLastChecked(LocalDateTime.now());
            apiStatus.setResponseTime(responseTime);
            apiStatus.setDetails(passed ? "All tests passed" : "API not responding");

            apiStatuses.add(apiStatus);
        }

        return apiStatuses;
    }
}
