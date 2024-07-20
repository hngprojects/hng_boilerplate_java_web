package hng_java_boilerplate.service;

import hng_java_boilerplate.dtos.requests.*;
import hng_java_boilerplate.dtos.responses.*;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountRecoveryImpl implements AccountRecovery{

    @Autowired
    private final UserRepository userRepository;

    @Override
    public RecoveryEmailResponse addRecoveryEmail(RecoveryEmailRequest request) {
        String recoveryEmail = request.getEmail();;
        if (!validateEmail(recoveryEmail)) {
            return RecoveryEmailResponse.builder().message("Invalid recovery email").build();
        }
        Optional<User> user = userRepository.findById("");
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setRecoveryEmail(recoveryEmail);
            userRepository.save(existingUser);
            return RecoveryEmailResponse.builder().message("Recovery email successfully added").build();
        }

        return RecoveryEmailResponse.builder().message("Could not add recovery email").build();
    }

    private boolean validateEmail(String recoveryEmail) {
        return recoveryEmail != null && recoveryEmail.contains("@");
    }

    @Override
    public DisplaySecurityQuestionsResponse displaySecurityQuestions() {
        Map<String, String> securityQuestions = new HashMap<>();
        securityQuestions.put("question_1", "What is your mother's maiden name?");
        securityQuestions.put("question_2", "In what city were you born?");
        securityQuestions.put("question_3", "What is the name of your first pet?");
        return DisplaySecurityQuestionsResponse.builder().questions(securityQuestions).message("Security Questions").build();
    }

    @Override
    public SecurityAnswersResponse submitSecurityQuestions(SubmitSecurityQuestionsRequest request) {
        List<SecurityQuestionAnswer> answers = request.getAnswers();

        if (validateAnswers(answers)) {
            Optional<User> user = userRepository.findById("");
            if (user.isPresent()) {
                User existingUser = user.get();
                existingUser.setSecurityAnswers(answers);
                userRepository.save(existingUser);
                return SecurityAnswersResponse.builder().message("Security answers submitted successfully").build();
            }
        }

        return SecurityAnswersResponse.builder().message("Could not submit security answers").build();
    }

    private boolean validateAnswers(List<SecurityQuestionAnswer> answers) {
        return answers != null && !answers.isEmpty();
    }

    @Override
    public RecoveryPhoneNumberResponse addRecoveryPhoneNumber(RecoveryPhoneNumberRequest request) {
        String recoveryPhoneNumber = request.getRecoveryPhoneNumber();
        if (!validatePhoneNumber(recoveryPhoneNumber)) {
            return RecoveryPhoneNumberResponse.builder().message("Invalid phone number").build();
        }

        Optional<User> user = userRepository.findById("");
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setRecoveryPhoneNumber(recoveryPhoneNumber);
            userRepository.save(existingUser);
            return RecoveryPhoneNumberResponse.builder().message("Recovery phone number successfully added").build();
        }

        return RecoveryPhoneNumberResponse.builder().message("Could not add recovery phone number").build();
    }

    private boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("\\d{10,15}");
    }

    @Override
    public UpdateRecoveryOptionsResponse updateRecoveryOptions(UpdateRecoveryOptionsRequest request) {
        boolean emailIsValid = validateEmail(request.getEmail());
        boolean phoneNumberIsValid = validatePhoneNumber(request.getPhoneNumber());
        boolean securityQuestionAnswersIsValid = validateSecurityQuestions(request.getSecurityQuestions());

        if (emailIsValid && phoneNumberIsValid && securityQuestionAnswersIsValid) {
            Optional<User> user = userRepository.findById("");
            if (user.isPresent()) {
                User existingUser = user.get();
                existingUser.setRecoveryPhoneNumber(request.getPhoneNumber());
                existingUser.setRecoveryEmail(request.getEmail());
                existingUser.setSecurityAnswers(request.getSecurityQuestions());
                userRepository.save(existingUser);
                return UpdateRecoveryOptionsResponse.builder().message("Recovery options updated").build();
            }
        }
        return UpdateRecoveryOptionsResponse.builder().message("Invalid recovery options").build();
    }

    @Override
    public DeleteRecoveryOptionsResponse deleteRecoveryOptions(DeleteRecoveryOptionsRequest request) {
        Optional<User> user = userRepository.findById("");
        List<String> options = request.getOptions();
        boolean isAValidRecoveryOption = validateRecoveryOptions(options);

        if (user.isPresent()) {
            User foundUser = user.get();
            if (isAValidRecoveryOption) {
                for (String option : options) {
                    switch (option.toLowerCase()) {
                        case "email":
                            foundUser.setRecoveryEmail(null);
                            break;
                        case "phoneNumber":
                            foundUser.setRecoveryPhoneNumber(null);
                            break;
                        case "securityQuestions":
                            foundUser.setSecurityAnswers(null);
                            break;
                        default:
                            return DeleteRecoveryOptionsResponse.builder().message("Invalid recovery option: " + option).build();
                    }
                }
                userRepository.save(foundUser);
                return DeleteRecoveryOptionsResponse.builder().message("Recovery options successfully deleted").build();
            } else {
                return DeleteRecoveryOptionsResponse.builder().message("Invalid recovery options").build();
            }
        }

        return DeleteRecoveryOptionsResponse.builder().message("User not found").build();
    }

    private boolean validateRecoveryOptions(List<String> options) {
        return options != null && !options.isEmpty() && options.size() <= 3;
    }

    private boolean validateSecurityQuestions(List<SecurityQuestionAnswer> securityQuestionAnswers) {
        return securityQuestionAnswers != null && !securityQuestionAnswers.isEmpty();
    }

}
