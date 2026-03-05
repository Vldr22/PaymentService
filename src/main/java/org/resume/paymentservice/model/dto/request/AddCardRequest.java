package org.resume.paymentservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AddCardRequest(
        @Schema(description = "Stripe PaymentMethod ID полученный от Stripe.js", example ="pi_3T4hhvtestyBFap60Hso8KXt")
        @NotBlank(message = "Payment method ID is required")
        String paymentMethodId
) {
}
