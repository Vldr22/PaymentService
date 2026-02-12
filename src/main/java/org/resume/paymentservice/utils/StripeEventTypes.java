package org.resume.paymentservice.utils;

import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class StripeEventTypes {

    public static final Set<String> SUPPORTED_EVENT_TYPES = Set.of(
            StripeEventTypes.PAYMENT_WEBHOOK_EVENT_SUCCESS,
            StripeEventTypes.PAYMENT_WEBHOOK_EVENT_PROCESSING,
            StripeEventTypes.PAYMENT_WEBHOOK_EVENT_CANCELED,
            StripeEventTypes.PAYMENT_WEBHOOK_EVENT_FAILED
    );

    public static final String PAYMENT_WEBHOOK_EVENT_SUCCESS = "payment_intent.succeeded";
    public static final String PAYMENT_WEBHOOK_EVENT_FAILED = "payment_intent.payment_failed";
    public static final String PAYMENT_WEBHOOK_EVENT_CANCELED = "payment_intent.canceled";
    public static final String PAYMENT_WEBHOOK_EVENT_PROCESSING = "payment_intent.processing";


}
