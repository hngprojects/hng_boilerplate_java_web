package hng_java_boilerplate.validEmail;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailComValidator.class)
public @interface ValidEmailCom {
    String message() default "Invalid email format, must end with .com";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
