package org.resume.paymentservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.resume.paymentservice.validation.RussianPhone;

public record ClientLoginRequest(
        @RussianPhone
        String phone,

        @NotBlank(message = "Code is required")
        String code
) {
}
