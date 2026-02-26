package org.resume.paymentservice.model.dto.request;

import jakarta.validation.constraints.NotNull;
import org.resume.paymentservice.model.enums.SubscriptionType;

public record CreateSubscriptionRequest(

        @NotNull(message = "Subscription type is required")
        SubscriptionType subscriptionType,

        @NotNull(message = "Saved card ID is required")
        Long savedCardId

) {
}
