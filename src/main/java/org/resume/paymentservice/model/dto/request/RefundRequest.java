package org.resume.paymentservice.model.dto.request;

import jakarta.validation.constraints.NotNull;
import org.resume.paymentservice.model.enums.RefundReason;

public record RefundRequest(
        @NotNull(message = "Refund reason is required")
        RefundReason reason
) {
}
