package hng_java_boilerplate.validEmail;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailComValidator implements ConstraintValidator<ValidEmailCom, String> {
    private static final String EMAIL_COM_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$";

    @Override
    public void initialize(ValidEmailCom constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && email.matches(EMAIL_COM_REGEX);
    }
}
