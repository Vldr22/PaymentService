package org.resume.paymentservice.exception;

import lombok.Getter;
import org.resume.paymentservice.utils.ErrorMessages;

@Getter
public class WebhookProcessingException extends RuntimeException {

    private final String eventId;

    public WebhookProcessingException(String message, String eventId) {
        super(message);
        this.eventId = eventId;
    }

    public WebhookProcessingException(String message, Throwable cause, String eventId) {
        super(message, cause);
        this.eventId = eventId;
    }

    public static WebhookProcessingException byAlreadyInProcess(String eventId) {
        return new WebhookProcessingException(
                String.format("%s%s", ErrorMessages.WEBHOOK_ALREADY_PROCESSED, eventId),
                eventId
        );
    }

    public static WebhookProcessingException byInvalidSignature(String eventId) {
        return new WebhookProcessingException(
                String.format("%s%s", ErrorMessages.WEBHOOK_INVALID_SIGNATURE, eventId),
                eventId
        );
    }

    public static WebhookProcessingException byProcessedFailed(String eventId, Throwable cause) {
        return new WebhookProcessingException(
                String.format("%s%s", ErrorMessages.WEBHOOK_PROCESSING_FAILED, eventId),
                cause,
                eventId
        );
    }

    public static WebhookProcessingException byDeserializationFailed(String eventId) {
        return new WebhookProcessingException(
                String.format("%s%s", ErrorMessages.WEBHOOK_DESERIALIZATION_FAILED, eventId),
                eventId
        );
    }

}
