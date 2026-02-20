package org.resume.paymentservice.service.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.model.enums.PaymentStatus;
import org.resume.paymentservice.model.enums.RefundStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundEventHandler {

    private final RefundService refundService;
    private final PaymentService paymentService;

    public void handleRefundSucceeded(String paymentIntentId) {
        log.info("Processing refund succeeded event for payment: {}", paymentIntentId);
        refundService.updateRefundStatusByPaymentIntentId(paymentIntentId, RefundStatus.SUCCEEDED);
        paymentService.updatePaymentStatus(paymentIntentId, PaymentStatus.REFUNDED);
    }

    public void handleRefundFailed(String stripeRefundId) {
        log.warn("Processing refund failed event: {}", stripeRefundId);
        refundService.updateRefundStatusByPaymentIntentId(stripeRefundId, RefundStatus.FAILED);
    }

}