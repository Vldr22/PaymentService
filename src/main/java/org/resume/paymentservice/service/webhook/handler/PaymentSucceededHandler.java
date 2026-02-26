package org.resume.paymentservice.service.webhook.handler;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.utils.StripeEventTypes;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentSucceededHandler implements WebhookEventHandler{

    private final PaymentEventHandler paymentEventHandler;
    private final BillingEventHandler billingEventHandler;

    @Override
    public void handle(Event event) {
        PaymentIntent paymentIntent = extractPaymentIntent(event);

        if (isBilling(paymentIntent)) {
            Long subscriptionId = extractSubscriptionId(paymentIntent);
            billingEventHandler.handleBillingSucceeded(paymentIntent.getId(), subscriptionId);
        } else {
            paymentEventHandler.handlePaymentSucceeded(paymentIntent.getId());
        }
    }

    @Override
    public String getEventType() {
        return StripeEventTypes.PAYMENT_SUCCESS;
    }
}
