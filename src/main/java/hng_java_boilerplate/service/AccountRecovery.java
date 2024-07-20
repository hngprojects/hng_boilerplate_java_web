package hng_java_boilerplate.service;

import hng_java_boilerplate.dtos.requests.*;
import hng_java_boilerplate.dtos.responses.*;

public interface AccountRecovery {

    RecoveryEmailResponse addRecoveryEmail(RecoveryEmailRequest request);

    DisplaySecurityQuestionsResponse displaySecurityQuestions();

    SecurityAnswersResponse submitSecurityQuestions(SubmitSecurityQuestionsRequest request);

    RecoveryPhoneNumberResponse addRecoveryPhoneNumber(RecoveryPhoneNumberRequest request);

    UpdateRecoveryOptionsResponse updateRecoveryOptions(UpdateRecoveryOptionsRequest request);

    DeleteRecoveryOptionsResponse deleteRecoveryOptions(DeleteRecoveryOptionsRequest request);

}
