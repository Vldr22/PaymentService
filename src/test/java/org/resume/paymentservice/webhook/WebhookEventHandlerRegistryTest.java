package org.resume.paymentservice.webhook;

import com.stripe.model.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.resume.paymentservice.service.webhook.WebhookEventHandlerRegistry;
import org.resume.paymentservice.service.webhook.handler.WebhookEventHandler;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WebhookEventHandlerRegistry — маршрутизация событий по обработчикам")
class WebhookEventHandlerRegistryTest {

    // dispatch — handler найден

    /**
     * Проверяет что событие передаётся нужному обработчику по типу события.
     */
    @Test
    void shouldDispatchEvent_toMatchingHandler() {
        WebhookEventHandler handler = mock(WebhookEventHandler.class);
        when(handler.getEventType()).thenReturn("payment_intent.succeeded");

        Event event = mock(Event.class);
        when(event.getType()).thenReturn("payment_intent.succeeded");

        WebhookEventHandlerRegistry registry = new WebhookEventHandlerRegistry(List.of(handler));
        registry.dispatch(event);

        verify(handler).handle(event);
    }

    // dispatch — handler не найден

    /**
     * Если для типа события нет обработчика — ничего не происходит,
     * исключений не бросается.
     */
    @Test
    void shouldSkipDispatch_whenNoHandlerFound() {
        WebhookEventHandler handler = mock(WebhookEventHandler.class);
        when(handler.getEventType()).thenReturn("payment_intent.succeeded");

        Event event = mock(Event.class);
        when(event.getType()).thenReturn("customer.created");

        WebhookEventHandlerRegistry registry = new WebhookEventHandlerRegistry(List.of(handler));
        registry.dispatch(event);

        verify(handler, never()).handle(any());
    }
}