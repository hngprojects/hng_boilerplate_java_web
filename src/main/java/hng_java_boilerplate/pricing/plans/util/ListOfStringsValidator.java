package hng_java_boilerplate.pricing.plans.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class ListOfStringsValidator implements ConstraintValidator<ValidList, List<String>> {
    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        for (String str : value) {
            if (str == null || str.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}