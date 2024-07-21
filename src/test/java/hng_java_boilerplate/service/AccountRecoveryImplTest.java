package hng_java_boilerplate.service;

import hng_java_boilerplate.dtos.requests.*;
import hng_java_boilerplate.dtos.responses.*;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AccountRecoveryImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountRecoveryImpl accountRecovery;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddRecoveryEmail_Success() {
        RecoveryEmailRequest request = new RecoveryEmailRequest("test@example.com");
        User user = new User();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        RecoveryEmailResponse response = accountRecovery.addRecoveryEmail(request);

        assertEquals("Recovery email successfully added", response.getMessage());
    }

    @Test
    public void testAddRecoveryEmail_InvalidEmail() {
        RecoveryEmailRequest request = new RecoveryEmailRequest("invalid-email");

        RecoveryEmailResponse response = accountRecovery.addRecoveryEmail(request);

        assertEquals("Invalid recovery email", response.getMessage());
    }

    @Test
    public void testDisplaySecurityQuestions() {
        DisplaySecurityQuestionsResponse response = accountRecovery.displaySecurityQuestions();

        assertEquals("Security Questions", response.getMessage());
        assertEquals(3, response.getQuestions().size());
    }

    @Test
    public void testSubmitSecurityQuestions_Success() {
        SecurityQuestionAnswer answer1 = new SecurityQuestionAnswer("question_1", "answer_1");
        SecurityQuestionAnswer answer2 = new SecurityQuestionAnswer("question_2", "answer_2");
        SecurityQuestionAnswer answer3 = new SecurityQuestionAnswer("question_3", "answer_3");

        SubmitSecurityQuestionsRequest request = new SubmitSecurityQuestionsRequest(
                List.of(answer1, answer2, answer3));

        User user = new User();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        SecurityAnswersResponse response = accountRecovery.submitSecurityQuestions(request);

        assertEquals("Security answers submitted successfully", response.getMessage());
    }

    @Test
    public void testSubmitSecurityQuestions_InvalidAnswers() {
        SubmitSecurityQuestionsRequest request = new SubmitSecurityQuestionsRequest(Collections.emptyList());

        SecurityAnswersResponse response = accountRecovery.submitSecurityQuestions(request);

        assertEquals("Could not submit security answers", response.getMessage());
    }

    @Test
    public void testAddRecoveryPhoneNumber_Success() {
        RecoveryPhoneNumberRequest request = new RecoveryPhoneNumberRequest("1234567890");
        User user = new User();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        RecoveryPhoneNumberResponse response = accountRecovery.addRecoveryPhoneNumber(request);

        assertEquals("Recovery phone number successfully added", response.getMessage());
    }

    @Test
    public void testAddRecoveryPhoneNumber_InvalidPhoneNumber() {
        RecoveryPhoneNumberRequest request = new RecoveryPhoneNumberRequest("invalid-phone");

        RecoveryPhoneNumberResponse response = accountRecovery.addRecoveryPhoneNumber(request);

        assertEquals("Invalid phone number", response.getMessage());
    }

    @Test
    public void testUpdateRecoveryOptions_Success() {
        UpdateRecoveryOptionsRequest request = new UpdateRecoveryOptionsRequest(
                "newemail@example.com", "1234567890",
                List.of(new SecurityQuestionAnswer("question_1", "answer_1")));

        User user = new User();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        UpdateRecoveryOptionsResponse response = accountRecovery.updateRecoveryOptions(request);

        assertEquals("Recovery options updated", response.getMessage());
    }

    @Test
    public void testUpdateRecoveryOptions_InvalidOptions() {
        UpdateRecoveryOptionsRequest request = new UpdateRecoveryOptionsRequest(
                "invalid-email", "invalid-phone", Collections.emptyList());

        UpdateRecoveryOptionsResponse response = accountRecovery.updateRecoveryOptions(request);

        assertEquals("Invalid recovery options", response.getMessage());
    }

    @Test
    public void testDeleteRecoveryOptions_Success() {
        DeleteRecoveryOptionsRequest request = new DeleteRecoveryOptionsRequest();
        request.setOptions(List.of("email", "phoneNumber"));
        User user = new User();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        DeleteRecoveryOptionsResponse response = accountRecovery.deleteRecoveryOptions(request);

        assertEquals("Recovery options successfully deleted", response.getMessage());
    }

    @Test
    public void testDeleteRecoveryOptions_InvalidOptions() {
        DeleteRecoveryOptionsRequest request = new DeleteRecoveryOptionsRequest();
        request.setOptions(List.of("invalid-option"));
        DeleteRecoveryOptionsResponse response = accountRecovery.deleteRecoveryOptions(request);
        assertEquals("Invalid recovery options", response.getMessage());
    }


}
