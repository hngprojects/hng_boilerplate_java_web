package hng_java_boilerplate.jobs.controller;

import hng_java_boilerplate.exception.ErrorResponseDto;
import hng_java_boilerplate.exception.ValidationError;
import hng_java_boilerplate.jobs.dto.ApiResponse;
import hng_java_boilerplate.jobs.entity.JobListing;
import hng_java_boilerplate.jobs.service.JobListingService;
import hng_java_boilerplate.region.dto.response.GetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
@Tag(name = "Job Listing", description = "Controller for Job Listing")
public class JobListingController {

    private final JobListingService jobListingService;

    public JobListingController(JobListingService jobListingService) {
        this.jobListingService = jobListingService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "create a job listing success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "User do not have admin privilege",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Invalid or missing required request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationError.class))
            ),
    })
    @Operation(summary = "create a job listing")
    public ResponseEntity<ApiResponse<JobListing>> createJobListing(@Valid @RequestBody JobListing jobListing) {
        JobListing createdJobListing = jobListingService.createJobListing(jobListing);
        ApiResponse<JobListing> response = new ApiResponse<>("Job listing created successfully", HttpStatus.CREATED.value(), createdJobListing);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "get job listing by ID success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Job listing do not exists with id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
    })
    @Operation(summary = "get a job listing using the job id")
    public ResponseEntity<ApiResponse<JobListing>> getJobListingById(@PathVariable Long id) {
        JobListing jobListing = jobListingService.getJobListingById(id);
        ApiResponse<JobListing> response = new ApiResponse<>("Job listing retrieved successfully", 200, jobListing);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    @Operation(description = "Get all Job listings")
    public ResponseEntity<ApiResponse<List<JobListing>>> getAllJobListings() {
        List<JobListing> jobListings = jobListingService.getAllJobListings();
        ApiResponse<List<JobListing>> response = new ApiResponse<>("Job listings retrieved successfully", 200, jobListings);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Update a job by id success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "User not authenticated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "user do not have admin privilege",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "jOb listing not found for the id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Invalid or missing required request data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationError.class))
            ),
    })
    @Operation(description = "Update a job listing using the job id. Only admin user can update")
    public ResponseEntity<ApiResponse<JobListing>> updateJobListing(@PathVariable Long id, @Valid @RequestBody JobListing jobListing) {
        JobListing updatedJob = jobListingService.updateJobListing(id, jobListing);
        ApiResponse<JobListing> response = new ApiResponse<>("Job listing updated successfully", 200, updatedJob);
        return ResponseEntity.ok(response);
    }
}
