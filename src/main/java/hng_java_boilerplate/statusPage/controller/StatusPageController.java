package hng_java_boilerplate.statusPage.controller;

import hng_java_boilerplate.statusPage.entity.StatusPage;
import hng_java_boilerplate.statusPage.service.StatusPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/api-status")
public class StatusPageController {

    private final StatusPageService statusPageService;

    @GetMapping
    public List<StatusPage> getAllApiStatuses() {
        return statusPageService.getAllApiStatuses();
    }

    @PostMapping
    public void updateApiStatus() {
        statusPageService.updateApiStatus();
    }
}
