package hng_java_boilerplate.jobs.controller;

import hng_java_boilerplate.jobs.dto.ApiResponse;
import hng_java_boilerplate.jobs.entity.JobListing;
import hng_java_boilerplate.jobs.service.JobListingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobListing>> getJobListingById(@PathVariable Long id) {
        JobListing jobListing = jobListingService.getJobListingById(id);
        ApiResponse<JobListing> response = new ApiResponse<>("Job listing retrieved successfully", 200, jobListing);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobListing>>> getAllJobListings() {
        List<JobListing> jobListings = jobListingService.getAllJobListings();
        ApiResponse<List<JobListing>> response = new ApiResponse<>("Job listings retrieved successfully", 200, jobListings);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobListing>> updateJobListing(@PathVariable Long id, @Valid @RequestBody JobListing jobListing) {
        JobListing updatedJob = jobListingService.updateJobListing(id, jobListing);
        ApiResponse<JobListing> response = new ApiResponse<>("Job listing updated successfully", 200, updatedJob);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteJobListing(@PathVariable Long id) {
        jobListingService.deleteJobListing(id);
        ApiResponse<String> response = new ApiResponse<>("Job listing deleted successfully", 200, "Job deleted with id: " + id);
        return ResponseEntity.ok(response);
    }
}
