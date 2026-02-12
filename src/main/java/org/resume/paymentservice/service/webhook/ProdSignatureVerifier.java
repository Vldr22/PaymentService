package org.resume.paymentservice.service.webhook;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.exception.WebhookProcessingException;
import org.resume.paymentservice.properties.StripeProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "stripe.webhook.dev-mode", havingValue = "false", matchIfMissing = true)
public class ProdSignatureVerifier implements WebhookSignatureVerifier {

    private static final String UNKNOWN = "unknown";
    private final StripeProperties stripeProperties;

    @PostConstruct
    public void init() {
        log.info("______________________________________");
        log.info("PROD SIGNATURE VERIFIER INITIALIZED!!!");
    }

    @Override
    public Event verifyWebhookEventSignature(String payload, String signatureHeader) {
        try {
            return Webhook.constructEvent(
                    payload,
                    signatureHeader,
                    stripeProperties.getWebhookSecret()
            );
        } catch (SignatureVerificationException e) {
            log.error("Invalid webhook signature");
            throw WebhookProcessingException.byInvalidSignature(UNKNOWN);
        }
    }
}
