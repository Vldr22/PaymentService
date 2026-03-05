package org.resume.paymentservice.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import org.resume.paymentservice.model.enums.BillingAttemptStatus;
import org.resume.paymentservice.model.enums.Currency;
import org.resume.paymentservice.model.enums.SubscriptionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BillingAttemptResponse(
        @Schema(description = "Номер попытки списания", example = "1")
        Integer attemptNumber,

        @Schema(description = "Статус попытки", example = "SUCCEEDED",
                allowableValues = {"PENDING", "SUCCEEDED", "FAILED"})
        BillingAttemptStatus status,

        @Schema(description = "Сообщение об ошибке при неудачной попытке", example = "Card declined")
        String errorMessage,

        @Schema(description = "Сумма списания", example = "9.99")
        BigDecimal amount,

        @Schema(description = "Валюта", example = "USD")
        Currency currency,

        @Schema(description = "Тип подписки", example = "BASIC")
        SubscriptionType subscriptionType,

        @Schema(description = "Дата и время запланированного списания", example = "2026-03-05T03:10:02.606315")
        LocalDateTime scheduledAt,

        @Schema(description = "Дата и время фактического списания", example = "2026-03-05T03:10:02.606315")
        LocalDateTime executedAt,

        @Schema(description = "Дата окончания оплаченного периода", example = "2026-03-05T03:10:02.606315")
        LocalDateTime periodEnd
) {
}
