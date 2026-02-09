package org.resume.paymentservice.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessages {

    // User errors
    public static final String USER_NOT_FOUND_BY_ID = "User not found with id: ";
    public static final String USER_NOT_FOUND_BY_EMAIL = "User not found with email: ";
    public static final String USER_NOT_FOUND_BY_PHONE = "User not found with phone: ";

    // Payment errors
    public static final String PAYMENT_NOT_FOUND = "Payment doesn't exist with id: ";
    public static final String PAYMENT_NOT_FOUND_BY_STRIPE_ID = "Payment doesn't exist with Stripe Id: ";

    // Stripe errors
    public static final String STRIPE_PAYMENT_CREATION_FAILED = "Stripe payment creation failed: ";
    public static final String STRIPE_PAYMENT_STATUS_Error = "Failed to get payment status from Stripe: ";

}
