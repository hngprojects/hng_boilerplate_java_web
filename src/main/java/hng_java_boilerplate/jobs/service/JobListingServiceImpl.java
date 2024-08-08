package hng_java_boilerplate.jobs.service;

import hng_java_boilerplate.jobs.entity.JobListing;
import hng_java_boilerplate.jobs.repository.JobListingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JobListingServiceImpl implements JobListingService {

    private final JobListingRepository jobListingRepository;

    public JobListingServiceImpl(JobListingRepository jobListingRepository) {
        this.jobListingRepository = jobListingRepository;
    }

    @Override
    public JobListing createJobListing(JobListing jobListing) {
        jobListing.setCreatedAt(LocalDateTime.now());
        return jobListingRepository.save(jobListing);
    }
}