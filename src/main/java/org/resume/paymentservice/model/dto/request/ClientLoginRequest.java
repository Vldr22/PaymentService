package org.resume.paymentservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.resume.paymentservice.validation.RussianPhone;

public record ClientLoginRequest(
        @Schema(description = "Номер телефона в российском формате", example = "+79001234567")
        @RussianPhone
        String phone,

        @Schema(description = "Код из SMS", example = "123456")
        @NotBlank(message = "Code is required")
        String code
) {
}
