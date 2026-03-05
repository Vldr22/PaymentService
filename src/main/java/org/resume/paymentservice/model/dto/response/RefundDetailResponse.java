package org.resume.paymentservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.resume.paymentservice.model.enums.Currency;
import org.resume.paymentservice.model.enums.RefundReason;
import org.resume.paymentservice.model.enums.RefundStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RefundDetailResponse(
        @Schema(description = "ID заявки на возврат", example = "7")
        Long refundId,

        @Schema(description = "Stripe Refund ID", example = "re_3T4hhvtestyBFap60Hso8KXt")
        String stripeRefundId,

        @Schema(description = "Stripe PaymentIntent ID", example = "pi_3T4hhvtestyBFap60Hso8KXt")
        String paymentIntentId,

        @Schema(description = "Сумма возврата", example = "9.99")
        BigDecimal amount,

        @Schema(description = "Валюта", example = "USD")
        Currency currency,

        @Schema(description = "Причина возврата: DUPLICATE — дублирующий платёж, FRAUDULENT — мошенничество, REQUESTED_BY_CUSTOMER — запрос клиента",
                example = "DUPLICATE")
        RefundReason reason,

        @Schema(description = "Статус заявки", example = "PENDING",
                allowableValues = {"PENDING", "APPROVED", "REJECTED", "SUCCEEDED", "FAILED"})
        RefundStatus status,

        @Schema(description = "Имя клиента", example = "Иван Иванов")
        String clientName,

        @Schema(description = "Телефон клиента", example = "+79001234567")
        String clientPhone,

        @Schema(description = "Дата создания заявки", example = "2026-03-05T03:10:02.606315")
        LocalDateTime createdAt,

        @Schema(description = "Дата рассмотрения заявки", example = "2026-03-05T03:10:02.606315")
        LocalDateTime reviewedAt
) {
}