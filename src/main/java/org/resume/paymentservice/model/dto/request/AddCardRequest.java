package org.resume.paymentservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddCardRequest(
        @NotBlank(message = "Payment method ID is required")
        String paymentMethodId
) {
}
