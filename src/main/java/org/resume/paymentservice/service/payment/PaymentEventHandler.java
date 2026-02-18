package org.resume.paymentservice.service.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.model.enums.PaymentStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventHandler {

    private final PaymentService paymentService;

    public void handlePaymentSucceeded(String stripePaymentIntentId) {
        log.info("Processing payment succeeded event: {}", stripePaymentIntentId);
        paymentService.updatePaymentStatus(stripePaymentIntentId, PaymentStatus.SUCCEEDED);
    }

    public void handlePaymentFailed(String stripePaymentIntentId) {
        log.info("Processing payment failed event: {}", stripePaymentIntentId);
        paymentService.updatePaymentStatus(stripePaymentIntentId, PaymentStatus.FAILED);
    }

    public void handlePaymentCanceled(String stripePaymentIntentId) {
        log.info("Processing payment canceled event: {}", stripePaymentIntentId);
        paymentService.updatePaymentStatus(stripePaymentIntentId, PaymentStatus.CANCELED);
    }

    public void handlePaymentProcessing(String stripePaymentIntentId) {
        log.info("Processing payment processing event: {}", stripePaymentIntentId);
        paymentService.updatePaymentStatus(stripePaymentIntentId, PaymentStatus.PROCESSING);
    }

}

