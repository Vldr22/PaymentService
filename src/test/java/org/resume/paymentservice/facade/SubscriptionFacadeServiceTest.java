package org.resume.paymentservice.facade;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.resume.paymentservice.model.dto.request.CreateSubscriptionRequest;
import org.resume.paymentservice.model.dto.response.BillingAttemptResponse;
import org.resume.paymentservice.model.dto.response.SubscriptionResponse;
import org.resume.paymentservice.model.entity.BillingAttempt;
import org.resume.paymentservice.model.entity.SavedCard;
import org.resume.paymentservice.model.entity.Subscription;
import org.resume.paymentservice.model.entity.User;
import org.resume.paymentservice.model.enums.SubscriptionType;
import org.resume.paymentservice.service.card.SavedCardService;
import org.resume.paymentservice.service.facade.SubscriptionFacadeService;
import org.resume.paymentservice.service.subscription.BillingAttemptService;
import org.resume.paymentservice.service.subscription.SubscriptionService;
import org.resume.paymentservice.service.user.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SubscriptionFacadeService — координация операций с подписками")
class SubscriptionFacadeServiceTest {

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private BillingAttemptService billingAttemptService;

    @Mock
    private SavedCardService savedCardService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SubscriptionFacadeService subscriptionFacadeService;

    private User user;
    private SavedCard savedCard;
    private Subscription subscription;

    @BeforeEach
    void setUp() {
        user = Instancio.create(User.class);
        savedCard = Instancio.create(SavedCard.class);

        subscription = Instancio.of(Subscription.class)
                .set(field(Subscription::getUser), user)
                .create();
    }

    // createSubscription

    /**
     * Проверяет создание подписки — карта и тип берутся из запроса,
     * результат маппится в SubscriptionResponse.
     */
    @Test
    void shouldCreateSubscription_andReturnResponse() {
        CreateSubscriptionRequest request = new CreateSubscriptionRequest(
                SubscriptionType.BASIC, savedCard.getId());

        when(userService.getCurrentUser()).thenReturn(user);
        when(savedCardService.getCardByIdAndUser(savedCard.getId(), user)).thenReturn(savedCard);
        when(subscriptionService.create(user, savedCard, SubscriptionType.BASIC)).thenReturn(subscription);

        SubscriptionResponse result = subscriptionFacadeService.createSubscription(request);

        assertThat(result).isNotNull();
        assertThat(result.subscriptionType()).isEqualTo(subscription.getSubscriptionType());
        verify(subscriptionService).create(user, savedCard, SubscriptionType.BASIC);
    }

    // getSubscription

    /**
     * Возвращает текущую подписку пользователя.
     */
    @Test
    void shouldReturnSubscription_forCurrentUser() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(subscriptionService.findByUserId(user.getId())).thenReturn(subscription);

        SubscriptionResponse result = subscriptionFacadeService.getSubscription();

        assertThat(result).isNotNull();
        assertThat(result.subscriptionStatus()).isEqualTo(subscription.getSubscriptionStatus());
    }

    // cancelSubscription

    /**
     * Проверяет отмену подписки, — подписка находится по пользователю
     * и передаётся в сервис для отмены.
     */
    @Test
    void shouldCancelSubscription_andReturnResponse() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(subscriptionService.findByUserId(user.getId())).thenReturn(subscription);

        SubscriptionResponse result = subscriptionFacadeService.cancelSubscription();

        assertThat(result).isNotNull();
        verify(subscriptionService).cancel(subscription);
    }

    // getBillingHistory

    /**
     * Проверяет получение истории биллинга, — список попыток списания
     * маппится в список ответов.
     */
    @Test
    void shouldReturnBillingHistory_asMappedResponseList() {
        List<BillingAttempt> attempts = List.of(
                Instancio.of(BillingAttempt.class)
                        .set(field(BillingAttempt::getSubscription), subscription)
                        .create(),
                Instancio.of(BillingAttempt.class)
                        .set(field(BillingAttempt::getSubscription), subscription)
                        .create()
        );

        when(userService.getCurrentUser()).thenReturn(user);
        when(subscriptionService.findByUserId(user.getId())).thenReturn(subscription);
        when(billingAttemptService.findAllBySubscriptionId(subscription.getId())).thenReturn(attempts);

        List<BillingAttemptResponse> result = subscriptionFacadeService.getBillingHistory();

        assertThat(result).hasSize(2);
        verify(billingAttemptService).findAllBySubscriptionId(subscription.getId());
    }
}
