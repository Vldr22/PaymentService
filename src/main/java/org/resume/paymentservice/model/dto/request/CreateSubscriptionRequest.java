package org.resume.paymentservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.resume.paymentservice.model.enums.SubscriptionType;

public record CreateSubscriptionRequest(
        @Schema(description = "Тип подписки", example = "BASIC")
        @NotNull(message = "Subscription type is required")
        SubscriptionType subscriptionType,

        @Schema(description = "ID сохранённой карты для списания", example = "42")
        @NotNull(message = "Saved card ID is required")
        Long savedCardId

) {
}
