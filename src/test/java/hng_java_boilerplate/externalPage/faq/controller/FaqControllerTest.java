package hng_java_boilerplate.externalPage.faq.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.externalPage.faq.dto.request.FaqRequest;
import hng_java_boilerplate.externalPage.faq.dto.response.CustomResponse;
import hng_java_boilerplate.externalPage.faq.dto.response.FaqResponse;
import hng_java_boilerplate.externalPage.faq.service.FaqService;
import hng_java_boilerplate.product.dto.ProductErrorDTO;
import hng_java_boilerplate.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(FaqController.class)
class FaqControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FaqService faqService;
    @MockBean
    private ProductErrorDTO productErrorDTO;
    @MockBean
    private JwtUtils jwtUtils;

    @Test
    void shouldCreateMockMvc() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    @WithMockUser
    void shouldGetFaqs() throws Exception {
        FaqResponse res1 = new FaqResponse("status", "1", "q1", "a1", null);
        FaqResponse res2 = new FaqResponse("status", "2", "q2", "a2", null);
        FaqResponse res3 = new FaqResponse("status", "3", "q3", "a3", null);

        List<FaqResponse> responses = List.of(res1, res2, res3);
        when(faqService.getFaqs()).thenReturn(responses);

        mockMvc.perform(get("/api/v1/faqs")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].status").value("status"))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].question").value("q1"))
                .andExpect(jsonPath("$[0].answer").value("a1"))
                .andExpect(jsonPath("$[1].status").value("status"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].question").value("q2"))
                .andExpect(jsonPath("$[1].answer").value("a2"))
                .andExpect(jsonPath("$[2].status").value("status"))
                .andExpect(jsonPath("$[2].id").value("3"))
                .andExpect(jsonPath("$[2].question").value("q3"))
                .andExpect(jsonPath("$[2].answer").value("a3"));

        verify(faqService, times(1)).getFaqs();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createFaqsWithNoRequest() throws Exception {
        mockMvc.perform(post("/api/v1/faqs")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateFaqs() throws Exception {
        FaqRequest request = new FaqRequest();
        request.setQuestion("q1");
        request.setAnswer("a1");

        FaqResponse res = new FaqResponse("success", "faq-id", "q1", "a1", null);
        when(faqService.createFaq(request)).thenReturn(res);

        ObjectMapper mapper = new ObjectMapper();
        String reqString = mapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/faqs")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.id").value(res.id()))
                .andExpect(jsonPath("$.question").value(res.question()))
                .andExpect(jsonPath("$.answer").value(res.answer()));

        verify(faqService, times(1)).createFaq(request);
    }

    @Test
    @WithMockUser
    void createFaqsWithoutAuthorization() throws Exception {
        FaqRequest request = new FaqRequest();
        request.setQuestion("q1");
        request.setAnswer("a1");

        ObjectMapper mapper = new ObjectMapper();
        String reqString = mapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/faqs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqString))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldUpdateFaq() throws Exception {
        FaqRequest request = new FaqRequest();
        request.setQuestion("q1");
        request.setAnswer("a1");

        CustomResponse res = new CustomResponse("success", "faq updated");
        when(faqService.updateFaq(request, "faq-id")).thenReturn(res);

        ObjectMapper mapper = new ObjectMapper();
        String reqString = mapper.writeValueAsString(request);
        mockMvc.perform(patch("/api/v1/faqs/faq-id", "faq-id")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(res.status()))
                .andExpect(jsonPath("$.message").value(res.message()));

        verify(faqService, times(1)).updateFaq(request, "faq-id");
    }

    @Test
    @WithMockUser
    void updateFaqWithoutAuthorization() throws Exception {
        FaqRequest request = new FaqRequest();
        request.setQuestion("q1");
        request.setAnswer("a1");

        ObjectMapper mapper = new ObjectMapper();
        String reqString = mapper.writeValueAsString(request);
        mockMvc.perform(patch("/api/v1/faqs/faq-id", "faq-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqString))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteFaq() throws Exception {
        CustomResponse res = new CustomResponse("success", "faq deleted");
        when(faqService.deleteFaq("faq-id")).thenReturn(res);

        mockMvc.perform(delete("/api/v1/faqs/faq-id")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(res.status()))
                .andExpect(jsonPath("$.message").value(res.message()));

        verify(faqService, times(1)).deleteFaq("faq-id");
    }

    @Test
    @WithMockUser
    void deleteFaqWithoutAuthorization() throws Exception {
        mockMvc.perform(delete("/api/v1/faqs/faq-id"))
                .andExpect(status().isForbidden());
    }
}