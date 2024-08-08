package hng_java_boilerplate.jobs.service;

import hng_java_boilerplate.helpCenter.topic.exceptions.ResourceNotFoundException;
import hng_java_boilerplate.jobs.entity.JobListing;
import hng_java_boilerplate.jobs.repository.JobListingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobListingServiceImpl implements JobListingService {

    private final JobListingRepository jobListingRepository;

    public JobListingServiceImpl(JobListingRepository jobListingRepository) {
        this.jobListingRepository = jobListingRepository;
    }

    @Override
    public JobListing createJobListing(JobListing jobListing) {
        LocalDateTime now = LocalDateTime.now();
        jobListing.setCreatedAt(now);
        jobListing.setUpdatedAt(now);
        return jobListingRepository.save(jobListing);
    }

    @Override
    public JobListing getJobListingById(Long id) {
        return jobListingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
    }

    @Override
    public List<JobListing> getAllJobListings() {
        return jobListingRepository.findAll();
    }
}