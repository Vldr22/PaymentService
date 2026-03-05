package org.resume.paymentservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.resume.paymentservice.model.enums.Currency;
import org.resume.paymentservice.model.enums.SubscriptionStatus;
import org.resume.paymentservice.model.enums.SubscriptionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubscriptionResponse(
        @Schema(description = "Тип подписки", example = "BASIC")
        SubscriptionType subscriptionType,

        @Schema(description = "Статус подписки", example = "ACTIVE",
                allowableValues = {"ACTIVE", "PAST_DUE", "SUSPENDED", "CANCELLED"})
        SubscriptionStatus subscriptionStatus,

        @Schema(description = "Сумма списания", example = "9.99")
        BigDecimal amount,

        @Schema(description = "Валюта", example = "USD")
        Currency currency,

        @Schema(description = "Дата следующего списания", example = "2026-03-05T03:10:02.606315")
        LocalDateTime nextBillingDate,

        @Schema(description = "Дата окончания подписки", example = "2026-03-05T03:10:02.606315")
        LocalDateTime endDate
) {
}
