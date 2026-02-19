package org.resume.paymentservice.model.dto.response;

import org.resume.paymentservice.model.enums.Currency;
import org.resume.paymentservice.model.enums.RefundReason;
import org.resume.paymentservice.model.enums.RefundStatus;

import java.math.BigDecimal;

public record RefundResponse(
        String paymentIntentId,
        BigDecimal amount,
        Currency currency,
        RefundReason reason,
        RefundStatus status
) {
}
