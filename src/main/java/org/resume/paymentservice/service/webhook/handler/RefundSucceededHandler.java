package org.resume.paymentservice.service.webhook.handler;

import com.stripe.model.Event;
import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.utils.StripeEventTypes;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefundSucceededHandler implements WebhookEventHandler {

    private final RefundEventHandler refundEventHandler;

    @Override
    public void handle(Event event) {
        String paymentIntentId = extractRefundPaymentIntentId(event);
        refundEventHandler.handleRefundSucceeded(paymentIntentId);
    }

    @Override
    public String getEventType() {
        return StripeEventTypes.REFUND_SUCCESS;
    }
}
