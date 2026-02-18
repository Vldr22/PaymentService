package org.resume.paymentservice.service.webhook;


import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.exception.NotFoundException;
import org.resume.paymentservice.exception.WebhookProcessingException;
import org.resume.paymentservice.model.entity.WebhookEvent;
import org.resume.paymentservice.repository.WebhookEventRepository;
import org.resume.paymentservice.service.payment.PaymentEventHandler;
import org.resume.paymentservice.utils.ErrorMessages;
import org.resume.paymentservice.utils.StripeEventTypes;
import org.springframework.stereotype.Service;
import com.stripe.model.Event;

import static org.resume.paymentservice.utils.StripeEventTypes.SUPPORTED_EVENT_TYPES;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final WebhookEventRepository webhookEventRepository;
    private final PaymentEventHandler paymentEventHandler;
    private final WebhookSignatureVerifier signatureVerifier;

    @Transactional
    public void createWebhookEvent(String payload, String signatureHeader) {
        Event event = verifyWebhookEventSignature(payload, signatureHeader);
        checkDuplicate(event.getId());

        if (!SUPPORTED_EVENT_TYPES.contains(event.getType())) {
            log.warn("Ignoring unsupported event type: {}", event.getType());
            return;
        }

        saveWebhookEventInDatabase(event, payload);
        handleWebhookEvent(event);
        markEventAsProcessed(event.getId());

        log.info("Webhook processed successfully: eventId={}", event.getId());
    }

    private void checkDuplicate(String eventId) {
        if (webhookEventRepository.existsByEventId(eventId)) {
            log.warn("Webhook event already processed: {}", eventId);
            throw WebhookProcessingException.byAlreadyInProcess(eventId);
        }
    }

    private String extractPaymentIntentId(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> WebhookProcessingException.byDeserializationFailed(event.getId()));

        return paymentIntent.getId();
    }


    private Event verifyWebhookEventSignature(String payload, String signatureHeader) {
        return signatureVerifier.verifyWebhookEventSignature(payload, signatureHeader);
    }

    private void saveWebhookEventInDatabase(Event event, String payload) {
        WebhookEvent webhookEvent = new WebhookEvent(
                event.getId(),
                event.getType(),
                false,
                payload
        );

        webhookEventRepository.save(webhookEvent);
        log.info("Saved webhook event in database: eventId={}, eventType={}", event.getId(), event.getType());
    }

    private void handleWebhookEvent(Event event) {
        String stripePaymentIntentId = extractPaymentIntentId(event);

        switch (event.getType()) {
            case StripeEventTypes.PAYMENT_WEBHOOK_EVENT_SUCCESS ->
                    paymentEventHandler.handlePaymentSucceeded(stripePaymentIntentId);
            case StripeEventTypes.PAYMENT_WEBHOOK_EVENT_PROCESSING ->
                    paymentEventHandler.handlePaymentProcessing(stripePaymentIntentId);
            case StripeEventTypes.PAYMENT_WEBHOOK_EVENT_CANCELED ->
                    paymentEventHandler.handlePaymentCanceled(stripePaymentIntentId);
            case StripeEventTypes.PAYMENT_WEBHOOK_EVENT_FAILED ->
                    paymentEventHandler.handlePaymentFailed(stripePaymentIntentId);
            default -> throw new IllegalStateException(
                    String.format("%s%s", ErrorMessages.WEBHOOK_UNHANDLED_SUPPORTED_EVENT, event.getType())
            );
        }
    }

    private void markEventAsProcessed(String eventId) {
        WebhookEvent webhookEvent = webhookEventRepository.findByEventId(eventId)
                .orElseThrow(() -> NotFoundException.webhookEventByID(eventId));

        webhookEvent.setProcessed(true);
        webhookEventRepository.save(webhookEvent);

        log.info("Webhook event marked as processed: {}", eventId);
    }

}
