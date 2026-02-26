package org.resume.paymentservice.service.webhook.handler;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.utils.StripeEventTypes;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentProcessingHandler implements WebhookEventHandler{

    private final PaymentEventHandler paymentEventHandler;

    @Override
    public void handle(Event event) {
        PaymentIntent paymentIntent = extractPaymentIntent(event);
        paymentEventHandler.handlePaymentProcessing(paymentIntent.getId());
    }

    @Override
    public String getEventType() {
        return StripeEventTypes.PAYMENT_PROCESSING;
    }

}
