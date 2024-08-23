package hng_java_boilerplate.externalPage.jobs.service;

import hng_java_boilerplate.externalPage.jobs.entity.JobListing;

import java.util.List;

public interface JobListingService {
    JobListing createJobListing(JobListing jobListing);
    JobListing getJobListingById(Long id);
    List<JobListing> getAllJobListings();
    JobListing updateJobListing(Long id, JobListing updatedJobListing);
}
