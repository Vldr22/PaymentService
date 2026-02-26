package org.resume.paymentservice.service.webhook;

import com.stripe.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.service.webhook.handler.WebhookEventHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class WebhookEventHandlerRegistry {

    private final Map<String, WebhookEventHandler> handlers;

    public WebhookEventHandlerRegistry(List<WebhookEventHandler> handlers) {
        this.handlers = new HashMap<>();

        for (WebhookEventHandler handler : handlers) {
            this.handlers.put(handler.getEventType(), handler);
        }

        log.info("Registered webhook handlers: {}", this.handlers.keySet());
    }

    public void dispatch(Event event) {
        WebhookEventHandler handler = handlers.get(event.getType());

        if (handler == null) {
            log.warn("No handler found for event type: {}", event.getType());
            return;
        }

        handler.handle(event);
    }

}
