package org.resume.paymentservice.exception;

import lombok.Getter;
import org.resume.paymentservice.utils.ErrorMessages;

@Getter
public class PaymentException extends RuntimeException {

    private final String identifier;

    private PaymentException(String message, String identifier) {
        super(message);
        this.identifier = identifier;
    }

    public static PaymentException notRefundable(String paymentIntentId, String status) {
        return new PaymentException(
                String.format("%s%s", ErrorMessages.PAYMENT_NOT_REFUNDABLE, status),
                paymentIntentId
        );
    }

    public static PaymentException refundNotPending(String refundId, String status) {
        return new PaymentException(
                String.format("%s%s", ErrorMessages.REFUND_NOT_PENDING, status),
                refundId
        );
    }
}
