package hng_java_boilerplate.jobs.controller;

import hng_java_boilerplate.jobs.dto.ApiResponse;
import hng_java_boilerplate.jobs.entity.JobListing;
import hng_java_boilerplate.jobs.service.JobListingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobListingController {

    private final JobListingService jobListingService;

    public JobListingController(JobListingService jobListingService) {
        this.jobListingService = jobListingService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JobListing>> createJobListing(@Valid @RequestBody JobListing jobListing) {
        JobListing createdJobListing = jobListingService.createJobListing(jobListing);
        ApiResponse<JobListing> response = new ApiResponse<>("Job listing created successfully", HttpStatus.CREATED.value(), createdJobListing);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
