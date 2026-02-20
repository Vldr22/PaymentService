package org.resume.paymentservice.utils;

import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class StripeEventTypes {

    // Prefix
    public static final String PAYMENT_EVENT_PREFIX = "payment_intent.";
    public static final String CHARGE_REFUND_EVENT_PREFIX = "charge.refund";
    public static final String REFUND_EVENT_PREFIX = "refund.";

    // Payment events
    public static final String PAYMENT_SUCCESS = "payment_intent.succeeded";
    public static final String PAYMENT_FAILED = "payment_intent.payment_failed";
    public static final String PAYMENT_CANCELED = "payment_intent.canceled";
    public static final String PAYMENT_PROCESSING = "payment_intent.processing";

    // Refund events
    public static final String REFUND_SUCCESS = "charge.refunded";
    public static final String REFUND_FAILED = "refund.failed";

    public static final Set<String> SUPPORTED_EVENT_TYPES = Set.of(
            PAYMENT_SUCCESS,
            PAYMENT_PROCESSING,
            PAYMENT_CANCELED,
            PAYMENT_FAILED,
            REFUND_SUCCESS,
            REFUND_FAILED
    );
}
