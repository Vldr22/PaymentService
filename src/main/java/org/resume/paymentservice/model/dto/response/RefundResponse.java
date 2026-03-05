package org.resume.paymentservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.resume.paymentservice.model.enums.Currency;
import org.resume.paymentservice.model.enums.RefundReason;
import org.resume.paymentservice.model.enums.RefundStatus;

import java.math.BigDecimal;

public record RefundResponse(
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
        RefundStatus status
) {
}
