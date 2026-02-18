package org.resume.paymentservice.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class CodeGenerator {

    private static final String PASSWORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
    private static final int PASSWORD_LENGTH = 12;
    private static final int VERIFICATION_CODE_MIN = 100_000;
    private static final int VERIFICATION_CODE_MAX = 1_000_000;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(PASSWORD_CHARS.charAt(SECURE_RANDOM.nextInt(PASSWORD_CHARS.length())));
        }
        return password.toString();
    }

    public static String generateVerificationCode() {
        int code = SECURE_RANDOM.nextInt(VERIFICATION_CODE_MIN, VERIFICATION_CODE_MAX);
        return String.valueOf(code);
    }

}
