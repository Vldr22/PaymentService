package org.resume.paymentservice.exception;

import lombok.Getter;
import org.resume.paymentservice.utils.ErrorMessages;

@Getter
public class StripePaymentException extends RuntimeException {

    private final String stripeError;

    private StripePaymentException(String message, String stripeError, Throwable cause) {
        super(message, cause);
        this.stripeError = stripeError;
    }

    public static StripePaymentException byCreationError(String stripeMessage, Throwable cause) {
        return new StripePaymentException(
                String.format("%s%s", ErrorMessages.STRIPE_PAYMENT_CREATION_FAILED, stripeMessage),
                stripeMessage,
                cause
        );
    }

    public static StripePaymentException byStatusError(String stripePaymentIntentId, Throwable cause) {
        return new StripePaymentException(
                String.format("%s%s", ErrorMessages.STRIPE_PAYMENT_STATUS_Error, stripePaymentIntentId),
                stripePaymentIntentId,
                cause
        );
    }
}