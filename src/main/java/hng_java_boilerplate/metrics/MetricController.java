package hng_java_boilerplate;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricController {

    private final PrometheusMeterRegistry prometheusMeterRegistry;

    @GetMapping
    public String getMetrics() {
        return prometheusMeterRegistry.scrape();
    }
}
