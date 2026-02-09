package org.resume.paymentservice.exception;

import lombok.Getter;
import org.resume.paymentservice.utils.ErrorMessages;

@Getter
public class UserNotFoundException extends RuntimeException {

    private final String identifier;

    private UserNotFoundException(String message, String identifier) {
        super(message);
        this.identifier = identifier;
    }

    public static UserNotFoundException byId(Long userId) {
        return new UserNotFoundException(
                String.format("%s%d", ErrorMessages.USER_NOT_FOUND_BY_ID, userId),
                String.valueOf(userId)
        );
    }

    public static UserNotFoundException byEmail(String email) {
        return new UserNotFoundException(
                String.format("%s%s", ErrorMessages.USER_NOT_FOUND_BY_EMAIL, email),
                email
        );
    }

    public static UserNotFoundException byPhone(String phone) {
        return new UserNotFoundException(
                String.format("%s%s", ErrorMessages.USER_NOT_FOUND_BY_PHONE, phone),
                phone
        );
    }
}