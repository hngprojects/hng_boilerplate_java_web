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

import static org.hamcrest.Matchers.*;
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

}