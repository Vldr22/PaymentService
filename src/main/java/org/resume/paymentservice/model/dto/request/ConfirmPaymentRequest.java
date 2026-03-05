package org.resume.paymentservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ConfirmPaymentRequest(

        @Schema(description = "Stripe PaymentMethod ID полученный от Stripe.js", example ="pi_3T4hhvtestyBFap60Hso8KXt")
        @NotBlank(message = "Payment method is required")
        String paymentMethod,

        @Schema(description = "URL для редиректа после подтверждения", example = "https://example.com/return")
        @NotBlank(message = "Return URL is required")
        String returnUrl
) {
}
