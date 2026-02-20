package org.resume.paymentservice.model.dto.response;

public record SavedCardResponse(
        Long id,
        String last4,
        String brand,
        Short expMonth,
        Short expYear,
        boolean defaultCard
) {
}
