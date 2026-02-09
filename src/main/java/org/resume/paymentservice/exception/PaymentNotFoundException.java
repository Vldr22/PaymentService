package org.resume.paymentservice.exception;

import lombok.Getter;
import org.resume.paymentservice.utils.ErrorMessages;

@Getter
public class PaymentNotFoundException extends RuntimeException {

    private final String identifier;

    private PaymentNotFoundException(String message, String identifier) {
        super(message);
        this.identifier = identifier;
    }

    public static PaymentNotFoundException byId(Long paymentId) {
        return new PaymentNotFoundException(
                String.format("%s%d", ErrorMessages.PAYMENT_NOT_FOUND, paymentId),
                String.valueOf(paymentId)
        );
    }

    public static PaymentNotFoundException byStripeId(String stripePaymentIntentId) {
        return new PaymentNotFoundException(
                String.format("%s%s", ErrorMessages.PAYMENT_NOT_FOUND_BY_STRIPE_ID, stripePaymentIntentId),
                stripePaymentIntentId
        );
    }
}