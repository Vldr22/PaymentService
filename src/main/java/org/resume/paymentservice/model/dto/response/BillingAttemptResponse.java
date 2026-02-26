package org.resume.paymentservice.model.dto.response;

import org.resume.paymentservice.model.enums.BillingAttemptStatus;

import java.time.LocalDateTime;

public record BillingAttemptResponse(
        Integer attemptNumber,
        BillingAttemptStatus status,
        String errorMessage,
        LocalDateTime scheduledAt,
        LocalDateTime executedAt
) {
}
