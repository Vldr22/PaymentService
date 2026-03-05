package org.resume.paymentservice.service.sheduler;

import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.model.entity.BillingAttempt;
import org.resume.paymentservice.model.entity.Subscription;
import org.resume.paymentservice.service.payment.PaymentService;
import org.resume.paymentservice.service.payment.StripeService;
import org.resume.paymentservice.service.subscription.BillingAttemptService;
import org.resume.paymentservice.service.subscription.SubscriptionService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingOrchestrator {

    private final SubscriptionService subscriptionService;
    private final BillingAttemptService billingAttemptService;
    private final StripeService stripeService;
    private final PaymentService paymentService;

    public void processSubscription(Subscription subscription) {
        log.info("Processing subscription: id={}, userId={}",
                subscription.getId(), subscription.getUser().getId());

        int attemptNumber = subscription.getRetryCount() + 1;
        BillingAttempt attempt = billingAttemptService.createPending(subscription, attemptNumber);

        try {
            PaymentIntent paymentIntent = stripeService.chargeWithSavedCard(
                    subscription.getAmount(),
                    subscription.getCurrency(),
                    subscription.getUser().getStripeCustomerId(),
                    subscription.getSavedCard().getStripePaymentMethodId(),
                    subscription.getSubscriptionType().name(),
                    subscription.getId()
            );

            attempt.setStripePaymentIntentId(paymentIntent.getId());
            billingAttemptService.save(attempt);
            paymentService.saveBillingPayment(paymentIntent, subscription);

            log.info("Charge initiated: subscriptionId={}, attemptId={}",
                    subscription.getId(), attempt.getId());

        } catch (Exception e) {
            log.error("Charge failed immediately: subscriptionId={}, error={}",
                    subscription.getId(), e.getMessage());

            billingAttemptService.markFailed(attempt, e.getMessage());
            subscriptionService.markFailed(subscription);
        }
    }
}
