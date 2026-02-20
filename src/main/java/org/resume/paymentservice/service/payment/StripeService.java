package org.resume.paymentservice.service.payment;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.exception.StripePaymentException;
import org.resume.paymentservice.model.dto.request.CreatePaymentRequest;
import org.resume.paymentservice.model.dto.response.PaymentResponse;
import org.resume.paymentservice.model.enums.RefundReason;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class StripeService {

    public PaymentResponse createStripePayment(CreatePaymentRequest request) {
        log.info("Creating Stripe payment: amount={}, currency={}",
                request.amount(), request.currency());

        try {
            long amountInCents = convertToCents(request.amount());

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(request.currency().name().toLowerCase())
                    .setDescription(request.description())
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            log.info("Payment Stripe has been created: id={}, status={}",
                    paymentIntent.getId(), paymentIntent.getStatus());

            return toPaymentResponse(paymentIntent);

        } catch (StripeException e) {
            log.error("Stripe payment creation failed: {}", e.getMessage(), e);
            throw StripePaymentException.byCreationError(e.getMessage(), e);
        }
    }


    public String createRefund(String paymentIntentId, RefundReason reason) {
        try {
            RefundCreateParams params = RefundCreateParams.builder()
                    .setPaymentIntent(paymentIntentId)
                    .setReason(mapRefundReason(reason))
                    .build();

            Refund stripeRefund = Refund.create(params);

            log.info("Stripe refund created: id={}, paymentIntent={}, status={}",
                    stripeRefund.getId(), paymentIntentId, stripeRefund.getStatus());

            return stripeRefund.getId();

        } catch (StripeException e) {
            log.error("Stripe refund failed: paymentIntent={}, error={}", paymentIntentId, e.getMessage());
            throw StripePaymentException.byRefundError(e.getMessage(), e);
        }
    }


    public PaymentResponse confirmPayment(String paymentIntentId, String paymentMethod, String returnUrl) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

             PaymentIntentConfirmParams params = PaymentIntentConfirmParams.builder()
                    .setPaymentMethod(paymentMethod)
                    .setReturnUrl(returnUrl)
                    .build();

            PaymentIntent confirmed = paymentIntent.confirm(params);

            log.info("Payment confirmed: id={}, status={}", confirmed.getId(), confirmed.getStatus());
            return toPaymentResponse(confirmed);

        } catch (StripeException e) {
            log.error("Payment confirmation failed: {}", e.getMessage(), e);
            throw StripePaymentException.byConfirmedError(e.getMessage(), e);
        }
    }

    public PaymentResponse getPaymentStatus(String paymentIntentId) {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            log.info("Stripe payment status retrieved: id={}, status={}",
                    paymentIntent.getId(), paymentIntent.getStatus());

            return toPaymentResponse(paymentIntent);

        } catch (StripeException e) {
            log.error("Failed to retrieve Stripe payment status: {}", e.getMessage(), e);
            throw StripePaymentException.byStatusError(e.getMessage(), e);
        }
    }


    private RefundCreateParams.Reason mapRefundReason(RefundReason reason) {
        return switch (reason) {
            case DUPLICATE -> RefundCreateParams.Reason.DUPLICATE;
            case FRAUDULENT -> RefundCreateParams.Reason.FRAUDULENT;
            case REQUESTED_BY_CUSTOMER -> RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER;
        };
    }

    private PaymentResponse toPaymentResponse(PaymentIntent paymentIntent) {
        return PaymentResponse.builder()
                .id(paymentIntent.getId())
                .status(paymentIntent.getStatus())
                .amount(paymentIntent.getAmount())
                .currency(paymentIntent.getCurrency())
                .clientSecret(paymentIntent.getClientSecret())
                .description(paymentIntent.getDescription())
                .build();
    }


    private long convertToCents(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).longValue();
    }

}
