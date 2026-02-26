package org.resume.paymentservice.service.webhook.handler;

import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import org.resume.paymentservice.contants.BillingConstants;
import org.resume.paymentservice.exception.WebhookProcessingException;

public interface WebhookEventHandler {

    void handle(Event event);

    String getEventType();

    default PaymentIntent extractPaymentIntent(Event event) {
        return (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> WebhookProcessingException.byDeserializationFailed(event.getId()));
    }

    default boolean isBilling(PaymentIntent paymentIntent) {
        return BillingConstants.METADATA_TYPE_BILLING
                .equals(paymentIntent.getMetadata().get(BillingConstants.METADATA_KEY_TYPE));
    }

    default Long extractSubscriptionId(PaymentIntent paymentIntent) {
        return Long.valueOf(
                paymentIntent.getMetadata().get(BillingConstants.METADATA_KEY_SUBSCRIPTION_ID));
    }

    default String extractRefundPaymentIntentId(Event event) {
        Object obj = event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> WebhookProcessingException.byDeserializationFailed(event.getId()));

        if (obj instanceof Charge charge) {
            return charge.getPaymentIntent();
        }

        throw WebhookProcessingException.byDeserializationFailed(event.getId());
    }

    default String extractRefundId(Event event) {
        Object obj = event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> WebhookProcessingException.byDeserializationFailed(event.getId()));

        if (obj instanceof com.stripe.model.Refund refund) {
            return refund.getId();
        }

        throw WebhookProcessingException.byDeserializationFailed(event.getId());
    }
}
