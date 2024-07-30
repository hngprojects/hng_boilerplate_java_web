package hng_java_boilerplate.job_test;

// JobControllerTest.java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import hng_java_boilerplate.job.controller.JobController;
import hng_java_boilerplate.job.models.Job;
import hng_java_boilerplate.job.service.JobService;

@WebMvcTest(JobController.class)
public class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

     @Test
    public void testGetAllJobs() throws Exception {
        when(jobService.getAllJobs()).thenReturn(Arrays.asList(new Job(), new Job()));

        mockMvc.perform(get("/api/v1/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("Jobs retrieved successfully"))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    public void testCreateJob() throws Exception {
        Job job = new Job();
        job.setTitle("Software Engineer");
        when(jobService.createJob(any(Job.class))).thenReturn(job);

        mockMvc.perform(post("/api/v1/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Software Engineer\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Software Engineer"));
    }

    @Test
    public void testGetJobById() throws Exception {
        Job job = new Job();
        job.setId(1L);
        job.setTitle("Software Engineer");
        when(jobService.getJobById(1L)).thenReturn(Optional.of(job));

        mockMvc.perform(get("/api/v1/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("Job retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Software Engineer"));
    }

    @Test
    public void testUpdateJob() throws Exception {
        Job job = new Job();
        job.setId(1L);
        job.setTitle("Updated Software Engineer");
        when(jobService.updateJob(eq(1L), any(Job.class))).thenReturn(job);

        mockMvc.perform(put("/api/v1/jobs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated Software Engineer\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Software Engineer"));
    }

    @Test
    public void testDeleteJob() throws Exception {
        doNothing().when(jobService).deleteJob(1L);

        mockMvc.perform(delete("/api/v1/jobs/1"))
                .andExpect(status().isOk());

        verify(jobService, times(1)).deleteJob(1L);
    }
}
