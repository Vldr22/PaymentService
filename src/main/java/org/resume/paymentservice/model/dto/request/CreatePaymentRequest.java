package org.resume.paymentservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.resume.paymentservice.model.enums.Currency;

import java.math.BigDecimal;

public record CreatePaymentRequest(

        @Schema(description = "Сумма платежа", example = "1500.00")
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be positive")
        @Digits(integer = 10, fraction = 2, message = "Invalid amount format")
        BigDecimal amount,


        @Schema(description = "Валюта платежа", example = "USD")
        @NotNull(message = "Currency is required")
        Currency currency,

        @Schema(description = "URL для редиректа после подтверждения", example = "https://example.com/return")
        @NotBlank(message = "Return URL is required")
        String returnUrl,

        @Schema(description = "Описание платежа", example = "Оплата заказа №12345")
        String description
) {
}
