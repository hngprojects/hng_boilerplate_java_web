package hng_java_boilerplate.about_page_test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.about_page.controller.AboutPageController;
import hng_java_boilerplate.about_page.entity.AboutPageDto;
import hng_java_boilerplate.about_page.service.AboutPageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AboutPageController.class)
class AboutPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AboutPageService aboutPageService;

    private AboutPageDto aboutPageDto;

    @BeforeEach
    void setUp() {
        aboutPageDto = new AboutPageDto();
        aboutPageDto.setId(1L);
        aboutPageDto.setTitle("New Title");
        aboutPageDto.setIntroduction("New Introduction");
    }

    @Test
    void testUpdateAboutPage_Success() throws Exception {
        when(aboutPageService.updateAboutPage(any(Long.class), any(AboutPageDto.class)))
                .thenReturn(aboutPageDto);

        mockMvc.perform(put("/api/v1/content/about/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(aboutPageDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("About page content updated successfully."))
                .andExpect(jsonPath("$.status_code").value(200));
    }




    @Test
    void testUpdateAboutPage_WrongId() throws Exception {
        when(aboutPageService.updateAboutPage(eq(2L), any(AboutPageDto.class)))
                .thenThrow(new RuntimeException("About page not found!"));

        mockMvc.perform(put("/api/v1/content/about/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(aboutPageDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Failed to update About page content."))
                .andExpect(jsonPath("$.status_code").value(404));
    }

}

