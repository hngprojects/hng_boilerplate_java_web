package hng_java_boilerplate.jobs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.jobs.entity.JobListing;
import hng_java_boilerplate.jobs.repository.JobListingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class JobListingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JobListingRepository jobListingRepository;

    @AfterEach
    void cleanup() {
        jobListingRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void createJobListing_shouldReturnCreatedJobListing() throws Exception {
        JobListing jobListing = new JobListing();
        jobListing.setTitle("Software Engineer");
        jobListing.setDescription("Develop amazing software");
        jobListing.setLocation("Remote");
        jobListing.setSalary("Competitive");
        jobListing.setJobType("Full-time");
        jobListing.setCompanyName("Tech Corp");

        mockMvc.perform(post("/api/v1/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jobListing)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("Job listing created successfully")))
                .andExpect(jsonPath("$.statusCode", is(201)))
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.title", is(jobListing.getTitle())))
                .andExpect(jsonPath("$.data.description", is(jobListing.getDescription())))
                .andExpect(jsonPath("$.data.location", is(jobListing.getLocation())))
                .andExpect(jsonPath("$.data.salary", is(jobListing.getSalary())))
                .andExpect(jsonPath("$.data.jobType", is(jobListing.getJobType())))
                .andExpect(jsonPath("$.data.companyName", is(jobListing.getCompanyName())))
                .andExpect(jsonPath("$.data.createdAt", notNullValue()));
    }

    @Test
    @WithMockUser
    void getJobListingById_shouldReturnJobListing() throws Exception {
        JobListing jobListing = new JobListing();
        jobListing.setTitle("Software Engineer");
        jobListing.setDescription("Develop amazing software");
        jobListing.setLocation("Remote");
        jobListing.setSalary("Competitive");
        jobListing.setJobType("Full-time");
        jobListing.setCompanyName("Tech Corp");
        jobListing.setCreatedAt(LocalDateTime.now());
        jobListing.setUpdatedAt(LocalDateTime.now());
        jobListing = jobListingRepository.save(jobListing);

        mockMvc.perform(get("/api/v1/jobs/{id}", jobListing.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Job listing retrieved successfully")))
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.data.id", is(jobListing.getId().intValue())))
                .andExpect(jsonPath("$.data.title", is(jobListing.getTitle())))
                .andExpect(jsonPath("$.data.description", is(jobListing.getDescription())))
                .andExpect(jsonPath("$.data.location", is(jobListing.getLocation())))
                .andExpect(jsonPath("$.data.salary", is(jobListing.getSalary())))
                .andExpect(jsonPath("$.data.jobType", is(jobListing.getJobType())))
                .andExpect(jsonPath("$.data.companyName", is(jobListing.getCompanyName())))
                .andExpect(jsonPath("$.data.createdAt", notNullValue()));
    }

    @Test
    @WithMockUser
    void getAllJobListings_shouldReturnListOfJobListings() throws Exception {
        JobListing jobListing1 = new JobListing();
        jobListing1.setTitle("Software Engineer");
        jobListing1.setDescription("Develop amazing software");
        jobListing1.setLocation("Remote");
        jobListing1.setSalary("Competitive");
        jobListing1.setJobType("Full-time");
        jobListing1.setCompanyName("Tech Corp");
        jobListing1.setCreatedAt(LocalDateTime.now());
        jobListing1.setUpdatedAt(LocalDateTime.now());
        jobListingRepository.save(jobListing1);

        JobListing jobListing2 = new JobListing();
        jobListing2.setTitle("Product Manager");
        jobListing2.setDescription("Manage product development");
        jobListing2.setLocation("San Francisco, CA");
        jobListing2.setSalary("Competitive");
        jobListing2.setJobType("Full-time");
        jobListing2.setCompanyName("InnovateX");
        jobListing2.setCreatedAt(LocalDateTime.now());
        jobListing2.setUpdatedAt(LocalDateTime.now());
        jobListingRepository.save(jobListing2);

        mockMvc.perform(get("/api/v1/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Job listings retrieved successfully")))
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].title", is(jobListing1.getTitle())))
                .andExpect(jsonPath("$.data[1].title", is(jobListing2.getTitle())));
    }

    @Test
    @WithMockUser
    void updateJobListing_shouldReturnUpdatedJobListing() throws Exception {
        JobListing jobListing = new JobListing();
        jobListing.setTitle("Software Engineer");
        jobListing.setDescription("Develop amazing software");
        jobListing.setLocation("Remote");
        jobListing.setSalary("Competitive");
        jobListing.setJobType("Full-time");
        jobListing.setCompanyName("Tech Corp");
        jobListing.setCreatedAt(LocalDateTime.now());
        jobListing.setUpdatedAt(LocalDateTime.now());
        jobListing = jobListingRepository.save(jobListing);

        JobListing updatedJobListing = new JobListing();
        updatedJobListing.setTitle("Senior Software Engineer");
        updatedJobListing.setDescription("Develop and lead software projects");
        updatedJobListing.setLocation("Remote");
        updatedJobListing.setSalary("Very Competitive");
        updatedJobListing.setJobType("Full-time");
        updatedJobListing.setCompanyName("Tech Corp");

        mockMvc.perform(put("/api/v1/jobs/{id}", jobListing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedJobListing)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Job listing updated successfully")))
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.data.title", is(updatedJobListing.getTitle())))
                .andExpect(jsonPath("$.data.description", is(updatedJobListing.getDescription())))
                .andExpect(jsonPath("$.data.location", is(updatedJobListing.getLocation())))
                .andExpect(jsonPath("$.data.salary", is(updatedJobListing.getSalary())))
                .andExpect(jsonPath("$.data.jobType", is(updatedJobListing.getJobType())))
                .andExpect(jsonPath("$.data.companyName", is(updatedJobListing.getCompanyName())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteJobListing_shouldReturnSuccessMessage() throws Exception {
        JobListing jobListing = new JobListing();
        jobListing.setTitle("Software Engineer");
        jobListing.setDescription("Develop amazing software");
        jobListing.setLocation("Remote");
        jobListing.setSalary("Competitive");
        jobListing.setJobType("Full-time");
        jobListing.setCompanyName("Tech Corp");
        jobListing.setCreatedAt(LocalDateTime.now());
        jobListing.setUpdatedAt(LocalDateTime.now());
        jobListing = jobListingRepository.save(jobListing);

        mockMvc.perform(delete("/api/v1/jobs/{id}", jobListing.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Job listing deleted successfully")))
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.data", is("Job deleted with id: " + jobListing.getId())));

        Optional<JobListing> deletedJobListing = jobListingRepository.findById(jobListing.getId());
        assertFalse(deletedJobListing.isPresent());
    }

}