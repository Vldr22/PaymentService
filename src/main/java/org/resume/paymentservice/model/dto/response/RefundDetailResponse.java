package org.resume.paymentservice.model.dto.response;

import org.resume.paymentservice.model.enums.Currency;
import org.resume.paymentservice.model.enums.RefundReason;
import org.resume.paymentservice.model.enums.RefundStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RefundDetailResponse(
        Long refundId,
        String stripeRefundId,
        String paymentIntentId,
        BigDecimal amount,
        Currency currency,
        RefundReason reason,
        RefundStatus status,
        String clientName,
        String clientPhone,
        LocalDateTime createdAt,
        LocalDateTime reviewedAt
) {
}