package hng_java_boilerplate.jobs.service;

import hng_java_boilerplate.exception.NotFoundException;
import hng_java_boilerplate.jobs.entity.JobListing;
import hng_java_boilerplate.jobs.repository.JobListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

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
    void testGetJobListingById_ValidId(){
        JobListing jobListing = new JobListing();
        jobListing.setId(1L);
        jobListing.setTitle("Software Engineer");
        jobListing.setDescription("Develop amazing software");
        jobListing.setLocation("Remote");
        jobListing.setSalary("Competitive");
        jobListing.setJobType("Full-time");
        jobListing.setCompanyName("Tech Corp");
        jobListing.setCreatedAt(LocalDateTime.now());

        when(jobListingRepository.findById(1L)).thenReturn(java.util.Optional.of(jobListing));

        JobListing result = jobListingService.getJobListingById(1L);

        assertNotNull(result);
        assertEquals(jobListing.getId(), result.getId());
        assertEquals(jobListing.getTitle(), result.getTitle());
        assertEquals(jobListing.getDescription(), result.getDescription());
        assertEquals(jobListing.getLocation(), result.getLocation());
        assertEquals(jobListing.getSalary(), result.getSalary());
        assertEquals(jobListing.getJobType(), result.getJobType());
        assertEquals(jobListing.getCompanyName(), result.getCompanyName());
        assertNotNull(result.getCreatedAt());

        verify(jobListingRepository, times(1)).findById(1L);
    }
    @Test
    void testGetJobListingById_InvalidId(){
        when(jobListingRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> jobListingService.getJobListingById(1L), "Job not found with id: 1");

        verify(jobListingRepository, times(1)).findById(1L);
    }
    @Test
    void testUpdateJob_InvalidData(){
        JobListing jobListing = new JobListing();
        jobListing.setId(1L);
        jobListing.setTitle("Software Engineer");
        jobListing.setDescription("Develop amazing software");
        jobListing.setLocation("Remote");
        jobListing.setSalary("Competitive");
        jobListing.setJobType("Full-time");
        jobListing.setCompanyName("Tech Corp");
        jobListing.setCreatedAt(LocalDateTime.now());

        when(jobListingRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> jobListingService.updateJobListing(1L, jobListing), "Job not found with id: 1");

        verify(jobListingRepository, times(1)).findById(1L);
    }
}
