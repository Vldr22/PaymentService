package org.resume.paymentservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static org.resume.paymentservice.utils.ErrorMessages.INVALID_PHONE_NUMBER;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RussianPhoneValidator.class)
public @interface RussianPhone {
    String message() default INVALID_PHONE_NUMBER;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
