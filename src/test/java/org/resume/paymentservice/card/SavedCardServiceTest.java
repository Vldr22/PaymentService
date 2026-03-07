package org.resume.paymentservice.card;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.resume.paymentservice.exception.AlreadyExistsException;
import org.resume.paymentservice.exception.NotFoundException;
import org.resume.paymentservice.model.dto.data.SavedCardData;
import org.resume.paymentservice.model.entity.SavedCard;
import org.resume.paymentservice.model.entity.User;
import org.resume.paymentservice.repository.SavedCardRepository;
import org.resume.paymentservice.service.card.SavedCardService;
import org.resume.paymentservice.service.payment.StripeService;
import org.resume.paymentservice.service.subscription.SubscriptionService;
import org.resume.paymentservice.service.user.UserService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SavedCardService — операции управления сохранёнными картами")
class SavedCardServiceTest {

    private static final String PAYMENT_METHOD_ID = "pm_test_123";
    private static final String STRIPE_CUSTOMER_ID = "cus_test_456";

    @Mock
    private SavedCardRepository savedCardRepository;

    @Mock
    private UserService userService;

    @Mock
    private StripeService stripeService;

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private SavedCardService savedCardService;

    private User user;
    private SavedCard card;

    @BeforeEach
    void setUp() {
        user = Instancio.of(User.class)
                .set(field(User::getStripeCustomerId), STRIPE_CUSTOMER_ID)
                .create();

        card = Instancio.of(SavedCard.class)
                .set(field(SavedCard::getUser), user)
                .set(field(SavedCard::getStripePaymentMethodId), PAYMENT_METHOD_ID)
                .set(field(SavedCard::isDefaultCard), false)
                .create();
    }

    // addCard

    /**
     * Проверяет успешное добавление карты когда Stripe Customer уже существует.
     * Карта сохраняется с данными полученными от Stripe.
     */
    @Test
    void shouldAddCard_whenCustomerAlreadyExists() {
        SavedCardData cardData = new SavedCardData(PAYMENT_METHOD_ID, "4242", "visa", (short) 12, (short) 26);

        when(savedCardRepository.existsByStripePaymentMethodIdAndUser(PAYMENT_METHOD_ID, user)).thenReturn(false);
        when(stripeService.addPaymentMethod(STRIPE_CUSTOMER_ID, PAYMENT_METHOD_ID)).thenReturn(cardData);

        SavedCard result = savedCardService.addCard(user, PAYMENT_METHOD_ID);

        assertThat(result.getLast4()).isEqualTo("4242");
        assertThat(result.getBrand()).isEqualTo("visa");
        assertThat(result.getUser()).isEqualTo(user);
        verify(savedCardRepository).save(any(SavedCard.class));
    }

    /**
     * Если у пользователя нет Stripe Customer ID — создаём нового Customer в Stripe,
     * обновляем пользователя и добавляем карту.
     */
    @Test
    void shouldCreateStripeCustomer_whenNotExists() {
        SavedCardData cardData = new SavedCardData(PAYMENT_METHOD_ID, "4242", "visa", (short) 12, (short) 26);

        user = Instancio.of(User.class)
                .set(field(User::getStripeCustomerId), null)
                .create();

        when(savedCardRepository.existsByStripePaymentMethodIdAndUser(PAYMENT_METHOD_ID, user)).thenReturn(false);
        when(stripeService.createCustomer(any(), any())).thenReturn(STRIPE_CUSTOMER_ID);
        when(stripeService.addPaymentMethod(STRIPE_CUSTOMER_ID, PAYMENT_METHOD_ID)).thenReturn(cardData);

        savedCardService.addCard(user, PAYMENT_METHOD_ID);

        verify(stripeService).createCustomer(any(), any());
        verify(userService).updateStripeCustomerId(user, STRIPE_CUSTOMER_ID);
    }

    /**
     * Нельзя добавить карту которая уже привязана к этому пользователю.
     */
    @Test
    void shouldThrowAlreadyExists_whenCardAlreadyAdded() {
        when(savedCardRepository.existsByStripePaymentMethodIdAndUser(PAYMENT_METHOD_ID, user)).thenReturn(true);

        assertThatThrownBy(() -> savedCardService.addCard(user, PAYMENT_METHOD_ID))
                .isInstanceOf(AlreadyExistsException.class);

        verify(stripeService, never()).addPaymentMethod(any(), any());
    }

    // getUserCards

    /** Возвращает список всех карт пользователя. */
    @Test
    void shouldReturnUserCards() {
        when(savedCardRepository.findAllByUser(user)).thenReturn(List.of(card));

        List<SavedCard> result = savedCardService.getUserCards(user);

        assertThat(result).hasSize(1).contains(card);
    }

    // removeCard

    /**
     * Проверяет успешное удаление карты — карта отвязывается от Stripe
     * и удаляется из БД.
     */
    @Test
    void shouldRemoveCard_whenNoActiveSubscription() {
        when(savedCardRepository.findByIdAndUser(card.getId(), user)).thenReturn(Optional.of(card));
        when(subscriptionService.hasActiveSubscriptionByCard(card.getId())).thenReturn(false);

        savedCardService.removeCard(user, card.getId());

        verify(stripeService).removePaymentMethod(card.getStripePaymentMethodId());
        verify(savedCardRepository).delete(card);
    }

    /**
     * Нельзя удалить карту если к ней привязана активная подписка.
     */
    @Test
    void shouldThrowAlreadyExists_whenCardLinkedToSubscription() {
        when(savedCardRepository.findByIdAndUser(card.getId(), user)).thenReturn(Optional.of(card));
        when(subscriptionService.hasActiveSubscriptionByCard(card.getId())).thenReturn(true);

        assertThatThrownBy(() -> savedCardService.removeCard(user, card.getId()))
                .isInstanceOf(AlreadyExistsException.class);

        verify(stripeService, never()).removePaymentMethod(any());
        verify(savedCardRepository, never()).delete(any());
    }

    // setDefaultCard

    /**
     * Проверяет установку карты по умолчанию — сбрасывается предыдущая
     * и устанавливается новая.
     */
    @Test
    void shouldSetDefaultCard_andResetPrevious() {
        when(savedCardRepository.findByIdAndUser(card.getId(), user)).thenReturn(Optional.of(card));

        SavedCard result = savedCardService.setDefaultCard(user, card.getId());

        assertThat(result.isDefaultCard()).isTrue();
        verify(savedCardRepository).resetDefaultCard(user);
        verify(savedCardRepository).setDefaultCard(card.getId(), user);
    }

    // getCardByIdAndUser

    /** Возвращает карту по ID если она принадлежит пользователю. */
    @Test
    void shouldReturnCard_whenBelongsToUser() {
        when(savedCardRepository.findByIdAndUser(card.getId(), user)).thenReturn(Optional.of(card));

        SavedCard result = savedCardService.getCardByIdAndUser(card.getId(), user);

        assertThat(result).isEqualTo(card);
    }

    /** Бросает NotFoundException если карта не найдена или принадлежит другому пользователю. */
    @Test
    void shouldThrowNotFound_whenCardNotFound() {
        when(savedCardRepository.findByIdAndUser(card.getId(), user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> savedCardService.getCardByIdAndUser(card.getId(), user))
                .isInstanceOf(NotFoundException.class);
    }
}
