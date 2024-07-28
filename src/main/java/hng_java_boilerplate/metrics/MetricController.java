package hng_java_boilerplate.metrics;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
public class MetricController {

    @Autowired
    private PrometheusMeterRegistry prometheusMeterRegistry;

    @GetMapping
    public String getMetrics() {
        return prometheusMeterRegistry.scrape();
    }
}
