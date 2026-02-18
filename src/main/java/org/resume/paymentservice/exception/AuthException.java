package org.resume.paymentservice.exception;

import org.resume.paymentservice.utils.ErrorMessages;

public class AuthException extends RuntimeException {
    private AuthException(String message) {
        super(message);
    }

    public static AuthException invalidCredentials() {
        return new AuthException(ErrorMessages.INVALID_CREDENTIALS);
    }
}
