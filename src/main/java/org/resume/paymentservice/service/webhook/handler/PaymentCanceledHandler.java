package org.resume.paymentservice.service.webhook.handler;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.utils.StripeEventTypes;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentCanceledHandler implements WebhookEventHandler {


    private final PaymentEventHandler paymentEventHandler;


    @Override
    public void handle(Event event) {
        PaymentIntent paymentIntent = extractPaymentIntent(event);
        paymentEventHandler.handlePaymentCanceled(paymentIntent.getId());
    }

    @Override
    public String getEventType() {
        return StripeEventTypes.PAYMENT_CANCELED;
    }
}
