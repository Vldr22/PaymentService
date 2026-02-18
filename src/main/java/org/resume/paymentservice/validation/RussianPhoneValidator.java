package org.resume.paymentservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class RussianPhoneValidator implements ConstraintValidator<RussianPhone, String> {

    private static final String RUSSIAN_PHONE_PATTERN = "^7\\d{10}$";

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        if (phone == null || phone.isBlank()) {
            return false;
        }

        String normalized = phone.trim().replaceAll("\\D", "");

        if (normalized.startsWith("8")) {
            normalized = "7" + normalized.substring(1);
        }

        return normalized.matches(RUSSIAN_PHONE_PATTERN);
    }
}
