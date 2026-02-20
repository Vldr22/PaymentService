package org.resume.paymentservice.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConfirmWithSavedCardRequest(
        @NotNull(message = "Saved card ID is required")
        Long savedCardId,
        @NotBlank(message = "Return URL is required")
        String returnUrl
) {
}
