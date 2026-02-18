package org.resume.paymentservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.resume.paymentservice.validation.RussianPhone;

public record ClientRegistrationRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Surname is required")
        String surname,

        String midname,

        @RussianPhone
        String phone
) {
}
