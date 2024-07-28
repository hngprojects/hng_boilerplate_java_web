package hng_java_boilerplate.helpCenter.faq.controller;

import hng_java_boilerplate.helpCenter.faq.controller.FaqController;
import hng_java_boilerplate.helpCenter.faq.dto.response.FaqResponse;
import hng_java_boilerplate.helpCenter.faq.service.FaqService;
import hng_java_boilerplate.product.dto.ProductErrorDTO;
import hng_java_boilerplate.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FaqController.class)
@AutoConfigureMockMvc(addFilters = false)
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
    void  shouldCreateMockMvc() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void shouldGetFaqs() throws Exception {
        FaqResponse res1 = new FaqResponse("status", "1", "q1", "a1");
        FaqResponse res2 = new FaqResponse("status", "2", "q2", "a2");
        FaqResponse res3 = new FaqResponse("status", "3", "q3", "a3");

        List<FaqResponse> responses = List.of(res1, res2, res3);
        when(faqService.getFaqs()).thenReturn(responses);

        mockMvc.perform(get("/api/v1/faqs"))
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
}