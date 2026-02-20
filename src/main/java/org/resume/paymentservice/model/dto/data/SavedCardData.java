package org.resume.paymentservice.model.dto.data;

public record SavedCardData(
        String stripePaymentMethodId,
        String last4,
        String brand,
        Short expMonth,
        Short expYear
) {
}
