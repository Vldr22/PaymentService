package org.resume.paymentservice.service.webhook;

import com.stripe.model.Event;

public interface WebhookSignatureVerifier {
    Event verifyWebhookEventSignature(String payload, String signatureHeader);
}
