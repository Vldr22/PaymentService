package org.resume.paymentservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ConfirmPaymentRequest(

        @NotBlank(message = "Payment method is required")
        String paymentMethod,

        @NotBlank(message = "Return URL is required")
        String returnUrl
) {
}
