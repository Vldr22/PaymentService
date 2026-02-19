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

    public static AlreadyExistsException employeeByEmail(String email) {
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

}
