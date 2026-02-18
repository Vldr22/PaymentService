package org.resume.paymentservice.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PhoneUtils {

    private static final String RUSSIAN_COUNTRY_CODE = "7";
    private static final String OLD_RUSSIAN_PREFIX = "8";

    public static String normalize(String phone) {
        String digitsOnly = phone.trim().replaceAll("\\D", "");

        if (digitsOnly.startsWith(OLD_RUSSIAN_PREFIX)) {
            digitsOnly = RUSSIAN_COUNTRY_CODE + digitsOnly.substring(1);
        }

        if (!digitsOnly.startsWith(RUSSIAN_COUNTRY_CODE)) {
            digitsOnly = RUSSIAN_COUNTRY_CODE + digitsOnly;
        }

        return digitsOnly;
    }
}
