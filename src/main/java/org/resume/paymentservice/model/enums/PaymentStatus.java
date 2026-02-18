package org.resume.paymentservice.model.enums;

public enum PaymentStatus {
    PENDING,
    PROCESSING,
    SUCCEEDED,
    FAILED,
    CANCELED;

    public static PaymentStatus mapStripeStatus(String stripeStatus) {
        return switch (stripeStatus.toLowerCase()) {
            case "requires_payment_method", "requires_confirmation", "requires_action" -> PaymentStatus.PENDING;
            case "processing" -> PaymentStatus.PROCESSING;
            case "succeeded" -> PaymentStatus.SUCCEEDED;
            case "canceled" -> PaymentStatus.CANCELED;
            default -> PaymentStatus.FAILED;
        };
    }
}