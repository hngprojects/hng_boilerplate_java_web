package hng_java_boilerplate.controller;


import hng_java_boilerplate.dtos.requests.*;
import hng_java_boilerplate.dtos.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
public class AccountRecovery {

    @Autowired
    private hng_java_boilerplate.service.AccountRecovery accountRecovery;

    @PostMapping("/add-recovery-email")
    public ResponseEntity<ApiResponse<?>> addRecoveryEmail(@RequestBody RecoveryEmailRequest request) {
        var response = accountRecovery.addRecoveryEmail(request);

        if (response.getMessage().equals("Invalid recovery email")) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(response.getMessage()).statusCode("400").build());
        }

        if (response.getMessage().equals("Recovery email successfully added")) {
            return ResponseEntity.ok().body(ApiResponse.builder().message(response.getMessage()).statusCode("200").build());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder().message("Could not add recovery email").statusCode("500").build());
    }


    @GetMapping("/security-questions")
    public ResponseEntity<ApiResponse<?>>displaySecurityQuestions() {
        var response = accountRecovery.displaySecurityQuestions();

        if (response.getMessage().equals("Security Questions")) {
            return ResponseEntity.ok().body(ApiResponse.builder().message(response.getMessage()).statusCode("200").data(response.getQuestions()).build());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder().message("Could not find security questions").statusCode("404").build());

    }

    @PostMapping("/submit-security-answers")
    public ResponseEntity<ApiResponse<?>> submitSecurityAnswers(@RequestBody SubmitSecurityQuestionsRequest request) {
        System.out.println("controller == " + request);
        var response = accountRecovery.submitSecurityQuestions(request);

        if (response.getMessage().equals("Security answers submitted successfully")) {
            return ResponseEntity.ok().body(ApiResponse.builder().message(response.getMessage()).statusCode("200").build());
        }

        if (response.getMessage().equals("Could not submit security answers")) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(response.getMessage()).statusCode("400").build());
        }


        return null;
    }

    @PostMapping("/recovery-number")
    public ResponseEntity<ApiResponse<?>> addRecoveryNumber(@RequestBody RecoveryPhoneNumberRequest request) {
        var response = accountRecovery.addRecoveryPhoneNumber(request);

        if (response.getMessage().equals("Invalid phone number")) {
            return ResponseEntity.badRequest().body(ApiResponse.builder().message(response.getMessage()).statusCode("400").build());
        }

        if (response.getMessage().equals("Recovery phone number successfully added")) {
            return ResponseEntity.ok().body(ApiResponse.builder().message(response.getMessage()).statusCode("200").build());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder().message("Could not add recovery phone number").statusCode("500").build());
    }

    @PutMapping("/update-recovery-options")
    public ResponseEntity<ApiResponse<?>> updateRecoveryOptions(@RequestBody UpdateRecoveryOptionsRequest request) {
        var response = accountRecovery.updateRecoveryOptions(request);

        if (response.getMessage().equals("Recovery options updated")) {
            return ResponseEntity.ok().body(ApiResponse.builder().message(response.getMessage()).statusCode("200").build());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder().message(response.getMessage()).statusCode("500").build());
    }

    @DeleteMapping("/delete-recovery-options")
    public ResponseEntity<ApiResponse<?>> deleteRecoveryOptions(@RequestBody DeleteRecoveryOptionsRequest request) {
    var response = accountRecovery.deleteRecoveryOptions(request);
        if (response.getMessage().equals("Recovery options successfully deleted")) {
            return ResponseEntity.ok().body(ApiResponse.builder().message(response.getMessage()).statusCode("200").build());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder().message("Error deleting recovery options").statusCode("400").build());

    }


}