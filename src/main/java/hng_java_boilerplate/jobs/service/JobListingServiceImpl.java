package hng_java_boilerplate.jobs.service;

import hng_java_boilerplate.exception.NotFoundException;
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
                .orElseThrow(() -> new NotFoundException("Job not found with id: " + id));
    }

    @Override
    public List<JobListing> getAllJobListings() {
        return jobListingRepository.findAll();
    }

    @Override
    public JobListing updateJobListing(Long id, JobListing updatedJobListing) {
        JobListing existingJobListing = getJobListingById(id);

        existingJobListing.setTitle(updatedJobListing.getTitle());
        existingJobListing.setDescription(updatedJobListing.getDescription());
        existingJobListing.setLocation(updatedJobListing.getLocation());
        existingJobListing.setSalary(updatedJobListing.getSalary());
        existingJobListing.setJobType(updatedJobListing.getJobType());
        existingJobListing.setCompanyName(updatedJobListing.getCompanyName());

        existingJobListing.setUpdatedAt(LocalDateTime.now());

        return jobListingRepository.save(existingJobListing);
    }
}