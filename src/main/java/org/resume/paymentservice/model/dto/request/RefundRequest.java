package org.resume.paymentservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.resume.paymentservice.model.enums.RefundReason;

public record RefundRequest(
        @Schema(description = "Причина возврата: DUPLICATE — дублирующий платёж, FRAUDULENT — мошенничество, REQUESTED_BY_CUSTOMER — запрос клиента",
                example = "DUPLICATE")
        @NotNull(message = "Refund reason is required")
        RefundReason reason
) {
}
