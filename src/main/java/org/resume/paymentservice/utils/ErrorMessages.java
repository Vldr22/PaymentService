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
    public static final String STRIPE_PAYMENT_CONFIRMED_FAILED = "Stripe payment confirmed failed: ";
    public static final String STRIPE_PAYMENT_STATUS_Error = "Failed to get payment status from Stripe: ";

    // Webhook errors
    public static final String WEBHOOK_INVALID_SIGNATURE = "Invalid webhook signature for this event: ";
    public static final String WEBHOOK_ALREADY_PROCESSED = "This webhook already was processed: ";
    public static final String WEBHOOK_PROCESSING_FAILED = "Failed webhook event: ";
    public static final String WEBHOOK_DESERIALIZATION_FAILED = "Can't deserialize PaymentIntent from event: ";
    public static final String WEBHOOK_NOT_FOUND_BY_ID = "Webhook not found with id: ";
    public static final String WEBHOOK_UNHANDLED_SUPPORTED_EVENT = "No handler for supported event type: ";

}
