package org.resume.paymentservice.service.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.model.entity.Subscription;
import org.resume.paymentservice.service.subscription.SubscriptionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingScheduler {

    private final SubscriptionService subscriptionService;
    private final BillingOrchestrator billingProcessorService;

    @Scheduled(cron = "${billing.cron}")
    public void processDueSubscriptions() {
        List<Subscription> dueSubscriptions = subscriptionService.findDueSubscriptions();

        if (dueSubscriptions.isEmpty()) {
            log.debug("No due subscriptions found");
            return;
        }

        log.info("Processing {} due subscriptions", dueSubscriptions.size());

        for (Subscription subscription : dueSubscriptions) {
            billingProcessorService.processSubscription(subscription);
        }
    }
}
