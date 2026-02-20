package org.resume.paymentservice.exception;

import lombok.Getter;
import org.resume.paymentservice.utils.ErrorMessages;

@Getter
public class NotFoundException extends RuntimeException {

    private final String identifier;

    // User
    private NotFoundException(String message, String identifier) {
        super(message);
        this.identifier = identifier;
    }

    public static NotFoundException userById(Long userId) {
        return new NotFoundException(
                String.format("%s%d", ErrorMessages.USER_NOT_FOUND_BY_ID, userId),
                String.valueOf(userId)
        );
    }

    public static NotFoundException userByEmail(String email) {
        return new NotFoundException(
                String.format("%s%s", ErrorMessages.USER_NOT_FOUND_BY_EMAIL, email),
                email
        );
    }

    public static NotFoundException userByPhone(String phone) {
        return new NotFoundException(
                String.format("%s%s", ErrorMessages.USER_NOT_FOUND_BY_PHONE, phone),
                phone
        );
    }

    // Payment
    public static NotFoundException paymentById(Long paymentId) {
        return new NotFoundException(
                String.format("%s%d", ErrorMessages.PAYMENT_NOT_FOUND, paymentId),
                String.valueOf(paymentId)
        );
    }

    public static NotFoundException paymentByStripeId(String stripePaymentIntentId) {
        return new NotFoundException(
                String.format("%s%s", ErrorMessages.PAYMENT_NOT_FOUND_BY_STRIPE_ID, stripePaymentIntentId),
                stripePaymentIntentId
        );
    }

    // Refund
    public static NotFoundException refundById(Long refundId) {
        return new NotFoundException(
                String.format("%s%d", ErrorMessages.REFUND_NOT_FOUND, refundId),
                String.valueOf(refundId)
        );
    }

    //Card
    public static NotFoundException cardById(Long id) {
        return new NotFoundException(
                String.format("%s%d",  ErrorMessages.CARD_NOT_FOUND, id),
                String.valueOf(id)
        );
    }

    public static NotFoundException refundByPaymentIntentId(String paymentIntentId) {
        return new NotFoundException(
                String.format("%s%s", ErrorMessages.REFUND_NOT_FOUND_BY_PAYMENT_INTENT, paymentIntentId),
                paymentIntentId
        );
    }

    // Webhook
    public static NotFoundException webhookEventByID(String eventId) {
        return new NotFoundException(
                String.format("%s%s", ErrorMessages.WEBHOOK_NOT_FOUND_BY_ID, eventId),
                eventId
        );
    }
}