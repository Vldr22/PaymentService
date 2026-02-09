package org.resume.paymentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.model.dto.PaymentCreationData;
import org.resume.paymentservice.model.dto.request.CreatePaymentRequest;
import org.resume.paymentservice.model.dto.response.PaymentResponse;
import org.resume.paymentservice.model.entity.Payment;
import org.resume.paymentservice.model.enums.PaymentStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentFacadeService {

    private final PaymentService paymentService;
    private final StripeService stripeService;

    public PaymentResponse createPayment(Long userId, CreatePaymentRequest request) {
        PaymentResponse stripeResponse = stripeService.createStripePayment(request);

        PaymentCreationData data = buildPaymentCreationData(userId, request, stripeResponse);
        paymentService.savePayment(data);

        log.info("Payment created successfully: userId={}, stripePaymentIntentId={}",
                userId, stripeResponse.getId());

        return stripeResponse;
    }

    public PaymentResponse getPaymentStatus(String paymentIntentId) {
        PaymentResponse stripeResponse = stripeService.getPaymentStatus(paymentIntentId);

        Payment payment = paymentService.findByStripePaymentIntentId(paymentIntentId);
        PaymentStatus newStatus = mapStripeStatus(stripeResponse.getStatus());

        if (!payment.getStatus().equals(newStatus)) {
            paymentService.updatePaymentStatus(paymentIntentId, newStatus);
        }

        return stripeResponse;
    }


    private PaymentCreationData buildPaymentCreationData(Long userId, CreatePaymentRequest request,
                                                         PaymentResponse stripeResponse) {
        return PaymentCreationData.builder()
                .userId(userId)
                .stripePaymentIntentId(stripeResponse.getId())
                .amount(request.amount())
                .currency(request.currency())
                .description(request.description())
                .clientSecret(stripeResponse.getClientSecret())
                .build();
    }

    private PaymentStatus mapStripeStatus(String stripeStatus) {
        return switch (stripeStatus.toLowerCase()) {
            case "requires_payment_method", "requires_confirmation", "requires_action" -> PaymentStatus.PENDING;
            case "processing" -> PaymentStatus.PROCESSING;
            case "succeeded" -> PaymentStatus.SUCCEEDED;
            case "canceled" -> PaymentStatus.CANCELED;
            default -> PaymentStatus.FAILED;
        };
    }

}
