package hng_java_boilerplate.pricing.plans.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ListOfStringsValidator.class})
public @interface ValidList {
    String message() default "Invalid list of strings";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}