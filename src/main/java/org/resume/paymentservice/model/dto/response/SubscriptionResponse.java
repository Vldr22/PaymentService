package org.resume.paymentservice.model.dto.response;

import org.resume.paymentservice.model.enums.Currency;
import org.resume.paymentservice.model.enums.SubscriptionStatus;
import org.resume.paymentservice.model.enums.SubscriptionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubscriptionResponse(
        SubscriptionType subscriptionType,
        SubscriptionStatus subscriptionStatus,
        BigDecimal amount,
        Currency currency,
        LocalDateTime nextBillingDate,
        LocalDateTime endDate
) {
}
