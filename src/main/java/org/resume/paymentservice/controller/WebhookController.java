package org.resume.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.service.webhook.WebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webhooks")
public class WebhookController {

    private static final String STRIPE_SIGNATURE_HEADER = "Stripe-Signature";

    private final WebhookService webhookService;

    @PostMapping("/stripe")
    public ResponseEntity<Void> stripe(
            @RequestBody String payload,
            @RequestHeader(STRIPE_SIGNATURE_HEADER) String signatureHeader
    ) {

        webhookService.createWebhookEvent(payload, signatureHeader);
        return ResponseEntity.ok().build();
    }

}
