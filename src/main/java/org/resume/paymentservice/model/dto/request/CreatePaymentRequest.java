package org.resume.paymentservice.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.resume.paymentservice.model.enums.Currency;

import java.math.BigDecimal;

public record CreatePaymentRequest(

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be positive")
        @Digits(integer = 10, fraction = 2, message = "Invalid amount format")
        BigDecimal amount,

        @NotNull(message = "Currency is required")
        Currency currency,

        @NotBlank(message = "Return URL is required")
        String returnUrl,

        String description
) {
}
