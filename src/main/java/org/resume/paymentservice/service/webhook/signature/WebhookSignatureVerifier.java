package org.resume.paymentservice.service.webhook.signature;

import com.stripe.model.Event;

public interface WebhookSignatureVerifier {
    Event verifyWebhookEventSignature(String payload, String signatureHeader);
}
