package hng_java_boilerplate.job.service;

// JobService.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hng_java_boilerplate.job.Repository.JobRepository;

import java.util.List;
import java.util.Optional;

import hng_java_boilerplate.job.Exceptions.ResourceNotFoundException;
import hng_java_boilerplate.job.models.Job;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public Job updateJob(Long id, Job jobDetails) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));

        job.setTitle(jobDetails.getTitle());
        job.setDescription(jobDetails.getDescription());
        job.setCompany(jobDetails.getCompany());
        job.setLocation(jobDetails.getLocation());
        job.setSalary(jobDetails.getSalary());

        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));

        jobRepository.delete(job);
    }
}
