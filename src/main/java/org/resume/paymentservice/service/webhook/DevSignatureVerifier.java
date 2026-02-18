package org.resume.paymentservice.service.webhook;

import com.stripe.model.Event;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.exception.WebhookProcessingException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "stripe.webhook.dev-mode", havingValue = "dev")
public class DevSignatureVerifier implements WebhookSignatureVerifier {

    private static final String INVALID = "dev-invalid-signature";
    private static final String TEST_EVENT = "test-event";
    private static final String PARSE_ERROR = "parse-error";

    @PostConstruct
    public void init() {
        log.info("_____________________________________");
        log.info("DEV SIGNATURE VERIFIER INITIALIZED!!!");
    }

    @Override
    public Event verifyWebhookEventSignature(String payload, String signatureHeader) {
        if (INVALID.equals(signatureHeader)) {
            log.error("DEV MODE: Simulating invalid signature");
            throw WebhookProcessingException.byInvalidSignature(TEST_EVENT);
        }

        try {
            return Event.GSON.fromJson(payload, Event.class);
        } catch (Exception e) {
            throw WebhookProcessingException.byInvalidSignature(PARSE_ERROR);
        }
    }
}
