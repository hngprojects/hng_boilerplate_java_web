package hng_java_boilerplate.statusPage.scheduler;

import hng_java_boilerplate.statusPage.entity.ApiStatus;
import hng_java_boilerplate.statusPage.service.ApiStatusService;
import hng_java_boilerplate.statusPage.util.NewmanResultParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestScheduler {

    private final ApiStatusService apiStatusService;

    @Value("${newman.collection.path}")
    private String newmanCollectionPath;

    @Value("${newman.environment.path}")
    private String newmanEnvironmentPath;

    @Value("${newman.output.path}")
    private String newmanOutputPath;

    @Scheduled(fixedRate = 900000) // 15 minutes
    public void runTests() {
        try {
            // Run Newman tests
            runNewmanTests();

            // Parse test results
            List<ApiStatus> apiStatuses = NewmanResultParser.parseNewmanResults(newmanOutputPath);

            // Update API statuses in the database
            for (ApiStatus apiStatus : apiStatuses) {
                apiStatusService.updateApiStatus(apiStatus);
            }
            log.info("API statuses updated successfully");
        } catch (IOException | InterruptedException e) {
            log.error("Error running tests: " + e.getMessage());
        }
    }

    private void runNewmanTests() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "newman", "run", newmanCollectionPath,
                "-e", newmanEnvironmentPath,
                "--reporters", "cli,json",
                "--reporter-json-export", newmanOutputPath
        );

        Process process = processBuilder.start();

        // Read the output of the Newman command
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Newman tests failed with exit code: " + exitCode);
        }
    }
}
