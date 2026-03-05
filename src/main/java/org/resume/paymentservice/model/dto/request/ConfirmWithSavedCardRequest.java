package org.resume.paymentservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConfirmWithSavedCardRequest(
        @Schema(description = "ID сохранённой карты", example = "42")
        @NotNull(message = "Saved card ID is required")
        Long savedCardId,

        @Schema(description = "URL для редиректа после подтверждения", example = "https://example.com/return")
        @NotBlank(message = "Return URL is required")
        String returnUrl
) {
}
