package hng_java_boilerplate.externalPage.faq.serviceImpl;

import hng_java_boilerplate.exception.exception_class.BadRequestException;
import hng_java_boilerplate.externalPage.faq.dto.request.FaqRequest;
import hng_java_boilerplate.externalPage.faq.dto.response.CustomResponse;
import hng_java_boilerplate.externalPage.faq.dto.response.FaqResponse;
import hng_java_boilerplate.externalPage.faq.entity.FAQ;
import hng_java_boilerplate.externalPage.faq.repository.FaqRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Service
@ExtendWith(MockitoExtension.class)
class FaqServiceImplTest {
    @Mock
    private FaqRepository faqRepository;
    @InjectMocks
    private FaqServiceImpl underTest;

    @Test
    void shouldCreateFaq() {
        FaqRequest request = new FaqRequest();
        request.setQuestion("why are you so handsome");
        request.setAnswer("cus i'm me mama's boy");

        when(faqRepository.saveAndFlush(any())).then(invocation -> {
            FAQ faq = invocation.getArgument(0);
            faq.setId("faq-id");
            faq.setQuestion(request.getQuestion());
            faq.setAnswer(request.getAnswer());
            return faq;
        });

        FaqResponse response = underTest.createFaq(request);
        ArgumentCaptor<FAQ> faqArgumentCaptor = ArgumentCaptor.forClass(FAQ.class);

        verify(faqRepository).saveAndFlush(faqArgumentCaptor.capture());
        FAQ captured = faqArgumentCaptor.getValue();

        assertThat(captured.getId()).isEqualTo("faq-id");
        assertThat(captured.getQuestion()).isEqualTo(request.getQuestion());
        assertThat(captured.getAnswer()).isEqualTo(request.getAnswer());

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo("success");
        assertThat(response.id()).isEqualTo("faq-id");
    }

    @Test
    void shouldGetFaqs() {
        FAQ faq1 = new FAQ("faq-id1", "q1", "a1", null);
        FAQ faq2 = new FAQ("faq-id2", "q2", "a2", null);
        FAQ faq3 = new FAQ("faq-id2", "q2", "a2", "works");

        when(faqRepository.findAll()).thenReturn(List.of(faq1, faq2, faq3));

        List<FaqResponse> responses = underTest.getFaqs();
        verify(faqRepository, times(1)).findAll();

        assertThat(responses.size()).isEqualTo(3);
        assertThat(responses).containsExactlyInAnyOrder(
                new FaqResponse("success", faq1.getId(), faq1.getQuestion(), faq1.getAnswer(), null),
                new FaqResponse("success", faq2.getId(), faq2.getQuestion(), faq2.getAnswer(), null),
                new FaqResponse("success", faq3.getId(), faq3.getQuestion(), faq3.getAnswer(), faq3.getCategory())
        );
    }

    @Test
    void shouldUpdateFaq() {
        FAQ faq1 = new FAQ("faq-id1", "q1", "a1", "damn");

        when(faqRepository.findById(faq1.getId())).thenReturn(Optional.of(faq1));

        FaqRequest request = new FaqRequest();
        request.setAnswer("a2");
        CustomResponse response = underTest.updateFaq(request, faq1.getId());

        verify(faqRepository, times(1)).findById(any());
        verify(faqRepository, times(1)).save(any());

        assertThat(response.status()).isEqualTo("success");
        assertThat(response.message()).isEqualTo("faq updated successfully");
    }

    @Test
    void updateFaqWhenNoRequestBody() {
        FAQ faq1 = new FAQ("faq-id1", "q1", "a1", null);
        when(faqRepository.findById(faq1.getId())).thenReturn(Optional.of(faq1));

        FaqRequest request = new FaqRequest();
        assertThatThrownBy(() -> underTest.updateFaq(request, faq1.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("you need to provide field to update");
    }

    @Test
    void updateFaqWithInvalidId() {
        FaqRequest request = new FaqRequest();
        assertThatThrownBy(() -> underTest.updateFaq(request, "invalid-id"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("invalid request");
    }

    @Test
    void shouldDeleteFaq() {
        FAQ faq1 = new FAQ("faq-id1", "q1", "a1", null);
        when(faqRepository.findById(faq1.getId())).thenReturn(Optional.of(faq1));

        CustomResponse response = underTest.deleteFaq(faq1.getId());
        verify(faqRepository, times(1)).findById(any());
        verify(faqRepository, times(1)).delete(faq1);

        assertThat(response.status()).isEqualTo("success");
        assertThat(response.message()).isEqualTo("faq deleted successfully");
    }
}