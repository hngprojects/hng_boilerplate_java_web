package hng_java_boilerplate.statusPage.controller;

import hng_java_boilerplate.statusPage.entity.ApiStatus;
import hng_java_boilerplate.statusPage.service.ApiStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/api-status")
public class ApiStatusController {

    private final ApiStatusService apiStatusService;

    @GetMapping
    public ResponseEntity<List<ApiStatus>> getAllApiStatuses() {
        return ResponseEntity.ok(apiStatusService.getAllApiStatuses());
    }

    @PostMapping
    public ResponseEntity<ApiStatus> updateApiStatus(@RequestBody ApiStatus apiStatus) {
        return ResponseEntity.ok(apiStatusService.updateApiStatus(apiStatus));
    }
}