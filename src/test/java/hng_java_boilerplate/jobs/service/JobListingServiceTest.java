package hng_java_boilerplate.jobs.service;

import hng_java_boilerplate.helpCenter.topic.exceptions.ResourceNotFoundException;
import hng_java_boilerplate.jobs.entity.JobListing;
import hng_java_boilerplate.jobs.repository.JobListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JobListingServiceTest {

    @Mock
    private JobListingRepository jobListingRepository;

    @InjectMocks
    private JobListingServiceImpl jobListingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createJobListing_shouldSaveAndReturnJobListing() {
        JobListing jobListing = new JobListing();
        jobListing.setTitle("Software Engineer");
        jobListing.setDescription("Develop amazing software");
        jobListing.setLocation("Remote");
        jobListing.setSalary("Competitive");
        jobListing.setJobType("Full-time");
        jobListing.setCompanyName("Tech Corp");

        JobListing savedJobListing = new JobListing();
        savedJobListing.setId(1L);
        savedJobListing.setTitle(jobListing.getTitle());
        savedJobListing.setDescription(jobListing.getDescription());
        savedJobListing.setLocation(jobListing.getLocation());
        savedJobListing.setSalary(jobListing.getSalary());
        savedJobListing.setJobType(jobListing.getJobType());
        savedJobListing.setCompanyName(jobListing.getCompanyName());
        savedJobListing.setCreatedAt(LocalDateTime.now());

        when(jobListingRepository.save(any(JobListing.class))).thenReturn(savedJobListing);

        JobListing result = jobListingService.createJobListing(jobListing);

        assertNotNull(result);
        assertEquals(savedJobListing.getId(), result.getId());
        assertEquals(jobListing.getTitle(), result.getTitle());
        assertEquals(jobListing.getDescription(), result.getDescription());
        assertEquals(jobListing.getLocation(), result.getLocation());
        assertEquals(jobListing.getSalary(), result.getSalary());
        assertEquals(jobListing.getJobType(), result.getJobType());
        assertEquals(jobListing.getCompanyName(), result.getCompanyName());
        assertNotNull(result.getCreatedAt());

        verify(jobListingRepository, times(1)).save(any(JobListing.class));
    }

    @Test
    void getJobListingById_shouldReturnJobListing_whenJobExists() {
        Long jobId = 1L;
        JobListing jobListing = new JobListing();
        jobListing.setId(jobId);
        jobListing.setTitle("Software Engineer");

        when(jobListingRepository.findById(jobId)).thenReturn(java.util.Optional.of(jobListing));

        JobListing result = jobListingService.getJobListingById(jobId);

        assertNotNull(result);
        assertEquals(jobId, result.getId());
        verify(jobListingRepository, times(1)).findById(jobId);
    }

    @Test
    void getJobListingById_shouldThrowException_whenJobDoesNotExist() {
        Long jobId = 1L;

        when(jobListingRepository.findById(jobId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> jobListingService.getJobListingById(jobId));
        verify(jobListingRepository, times(1)).findById(jobId);
    }

    @Test
    void getAllJobListings_shouldReturnListOfJobListings() {
        JobListing jobListing1 = new JobListing();
        jobListing1.setTitle("Software Engineer");

        JobListing jobListing2 = new JobListing();
        jobListing2.setTitle("Data Scientist");

        List<JobListing> jobListings = List.of(jobListing1, jobListing2);

        when(jobListingRepository.findAll()).thenReturn(jobListings);

        List<JobListing> result = jobListingService.getAllJobListings();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(jobListingRepository, times(1)).findAll();
    }

    @Test
    void updateJobListing_shouldUpdateAndReturnUpdatedJobListing() {
        Long jobId = 1L;
        JobListing existingJobListing = new JobListing();
        existingJobListing.setId(jobId);
        existingJobListing.setTitle("Software Engineer");

        JobListing updatedJobListing = new JobListing();
        updatedJobListing.setTitle("Senior Software Engineer");
        updatedJobListing.setDescription("Develop and design software");

        when(jobListingRepository.findById(jobId)).thenReturn(java.util.Optional.of(existingJobListing));
        when(jobListingRepository.save(any(JobListing.class))).thenReturn(existingJobListing);

        JobListing result = jobListingService.updateJobListing(jobId, updatedJobListing);

        assertNotNull(result);
        assertEquals(jobId, result.getId());
        assertEquals("Senior Software Engineer", result.getTitle());
        assertEquals("Develop and design software", result.getDescription());
        verify(jobListingRepository, times(1)).findById(jobId);
        verify(jobListingRepository, times(1)).save(existingJobListing);
    }

    @Test
    void deleteJobListing_shouldDeleteJobListing_whenJobExists() {
        Long jobId = 1L;
        JobListing jobListing = new JobListing();
        jobListing.setId(jobId);

        when(jobListingRepository.findById(jobId)).thenReturn(java.util.Optional.of(jobListing));

        jobListingService.deleteJobListing(jobId);

        verify(jobListingRepository, times(1)).findById(jobId);
        verify(jobListingRepository, times(1)).delete(jobListing);
    }

    @Test
    void deleteJobListing_shouldThrowException_whenJobDoesNotExist() {
        Long jobId = 1L;

        when(jobListingRepository.findById(jobId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> jobListingService.deleteJobListing(jobId));
        verify(jobListingRepository, times(1)).findById(jobId);
    }
}
