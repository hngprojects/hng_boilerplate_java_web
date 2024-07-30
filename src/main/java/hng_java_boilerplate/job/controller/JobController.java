package hng_java_boilerplate.job.controller;

// JobController.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import hng_java_boilerplate.job.Exceptions.ResourceNotFoundException;
import hng_java_boilerplate.job.models.Job;
import hng_java_boilerplate.job.models.ResponseWrapper;
import hng_java_boilerplate.job.service.JobService;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<Job>>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        ResponseWrapper<List<Job>> response = new ResponseWrapper<>("200", "Jobs retrieved successfully", jobs);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public Job createJob(@RequestBody Job job) {
        return jobService.createJob(job);
    }

   @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Job>> getJobById(@PathVariable Long id) {
        return jobService.getJobById(id)
                .map(job -> {
                    ResponseWrapper<Job> response = new ResponseWrapper<>("200", "Job retrieved successfully", job);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseWrapper<>("404", "Job not found")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        try {
            Job updatedJob = jobService.updateJob(id, jobDetails);
            return ResponseEntity.ok(updatedJob);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        try {
            jobService.deleteJob(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
