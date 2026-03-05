package org.resume.paymentservice.service.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.model.dto.request.CreateSubscriptionRequest;
import org.resume.paymentservice.model.dto.response.BillingAttemptResponse;
import org.resume.paymentservice.model.dto.response.SubscriptionResponse;
import org.resume.paymentservice.model.entity.BillingAttempt;
import org.resume.paymentservice.model.entity.SavedCard;
import org.resume.paymentservice.model.entity.Subscription;
import org.resume.paymentservice.model.entity.User;
import org.resume.paymentservice.service.card.SavedCardService;
import org.resume.paymentservice.service.subscription.BillingAttemptService;
import org.resume.paymentservice.service.subscription.SubscriptionService;
import org.resume.paymentservice.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionFacadeService {

    private final SubscriptionService subscriptionService;
    private final BillingAttemptService billingAttemptService;
    private final SavedCardService savedCardService;
    private final UserService userService;

    public SubscriptionResponse createSubscription(CreateSubscriptionRequest request) {
        User user = userService.getCurrentUser();
        SavedCard savedCard = savedCardService.getCardByIdAndUser(request.savedCardId(), user);

        Subscription subscription = subscriptionService.create(
                user, savedCard, request.subscriptionType());

        log.info("Subscription created via facade: id={}, userId={}", subscription.getId(), user.getId());
        return toSubscriptionResponse(subscription);
    }

    public SubscriptionResponse getSubscription() {
        User user = userService.getCurrentUser();
        Subscription subscription = subscriptionService.findByUserId(user.getId());
        return toSubscriptionResponse(subscription);
    }

    public SubscriptionResponse cancelSubscription() {
        User user = userService.getCurrentUser();
        Subscription subscription = subscriptionService.findByUserId(user.getId());
        subscriptionService.cancel(subscription);

        log.info("Subscription cancelled via facade: id={}, userId={}", subscription.getId(), user.getId());
        return toSubscriptionResponse(subscription);
    }

    public List<BillingAttemptResponse> getBillingHistory() {
        User user = userService.getCurrentUser();
        Subscription subscription = subscriptionService.findByUserId(user.getId());
        return billingAttemptService.findAllBySubscriptionId(subscription.getId())
                .stream()
                .map(this::toBillingAttemptResponse)
                .toList();
    }

    private SubscriptionResponse toSubscriptionResponse(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.getSubscriptionType(),
                subscription.getSubscriptionStatus(),
                subscription.getAmount(),
                subscription.getCurrency(),
                subscription.getNextBillingDate(),
                subscription.getEndDate()
        );
    }

    private BillingAttemptResponse toBillingAttemptResponse(BillingAttempt attempt) {
        Subscription subscription = attempt.getSubscription();
        return new BillingAttemptResponse(
                attempt.getAttemptNumber(),
                attempt.getStatus(),
                attempt.getErrorMessage(),
                subscription.getAmount(),
                subscription.getCurrency(),
                subscription.getSubscriptionType(),
                attempt.getScheduledAt(),
                attempt.getExecutedAt(),
                subscription.getEndDate()
        );
    }

}
