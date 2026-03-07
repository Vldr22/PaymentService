package org.resume.paymentservice.service.webhook.handler;

import com.stripe.model.Event;
import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.utils.StripeEventTypes;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefundFailedHandler implements WebhookEventHandler {

   private final RefundEventHandler refundEventHandler;

    public void handle(Event event) {
        String paymentIntentId = extractRefundPaymentIntentIdFromRefund(event);
        refundEventHandler.handleRefundFailed(paymentIntentId);
    }

    @Override
    public String getEventType() {
        return StripeEventTypes.REFUND_FAILED;
    }

}
