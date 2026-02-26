package org.resume.paymentservice.exception;

import lombok.Getter;
import org.resume.paymentservice.utils.ErrorMessages;

@Getter
public class AlreadyExistsException extends RuntimeException {

    private final String identifier;

    private AlreadyExistsException(String message, String identifier) {
        super(message);
        this.identifier = identifier;
    }

    public static AlreadyExistsException userByPhone(String phone) {
        return new AlreadyExistsException(
                String.format("%s%s", ErrorMessages.PHONE_ALREADY_REGISTERED, phone),
                phone
        );
    }

    public static AlreadyExistsException staffByEmail(String email) {
        return new AlreadyExistsException(
                String.format("%s%s", ErrorMessages.EMAIL_ALREADY_REGISTERED, email),
                email
        );
    }

    public static AlreadyExistsException refundByPaymentId(Long paymentId) {
        return new AlreadyExistsException(
                String.format("%s%d", ErrorMessages.REFUND_ALREADY_REQUESTED, paymentId),
                String.valueOf(paymentId)
        );
    }

    public static AlreadyExistsException cardAlreadyPresent(String paymentMethodId) {
        return new AlreadyExistsException(
                String.format("%s%s", ErrorMessages.CARD_ALREADY_ATTACHED, paymentMethodId),
                paymentMethodId
        );
    }

    public static AlreadyExistsException subscriptionByUserId(Long userId) {
        return new AlreadyExistsException(
                String.format("%s%d", ErrorMessages.SUBSCRIPTION_ALREADY_EXISTS, userId),
                String.valueOf(userId)
        );
    }

    public static AlreadyExistsException cardLinkedToActiveSubscription(Long cardId) {
        return new AlreadyExistsException(
                String.format("%s%d", ErrorMessages.CARD_LINKED_TO_ACTIVE_SUBSCRIPTION, cardId),
                String.valueOf(cardId)
        );
    }

}
