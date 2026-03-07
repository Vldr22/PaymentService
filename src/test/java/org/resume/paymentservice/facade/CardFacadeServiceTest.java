package org.resume.paymentservice.facade;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.resume.paymentservice.model.dto.request.AddCardRequest;
import org.resume.paymentservice.model.dto.response.SavedCardResponse;
import org.resume.paymentservice.model.entity.SavedCard;
import org.resume.paymentservice.model.entity.User;
import org.resume.paymentservice.service.card.SavedCardService;
import org.resume.paymentservice.service.facade.CardFacadeService;
import org.resume.paymentservice.service.user.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CardFacadeService — управление картами клиентов")
class CardFacadeServiceTest {

    @Mock
    private SavedCardService savedCardService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CardFacadeService cardFacadeService;

    private User currentUser;
    private SavedCard card;

    @BeforeEach
    void setUp() {
        currentUser = Instancio.create(User.class);

        card = Instancio.of(SavedCard.class)
                .set(field(SavedCard::getUser), currentUser)
                .set(field(SavedCard::isDefaultCard), false)
                .create();
    }

    // addCard

    /**
     * Проверяет добавление карты для текущего пользователя.
     * Результат маппится в SavedCardResponse с корректными полями.
     */
    @Test
    void shouldAddCard_andReturnResponse() {
        AddCardRequest request = new AddCardRequest("pm_test_123");

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(savedCardService.addCard(currentUser, request.paymentMethodId())).thenReturn(card);

        SavedCardResponse result = cardFacadeService.addCard(request);

        assertThat(result.id()).isEqualTo(card.getId());
        assertThat(result.last4()).isEqualTo(card.getLast4());
        assertThat(result.brand()).isEqualTo(card.getBrand());
        verify(savedCardService).addCard(currentUser, request.paymentMethodId());
    }

    // getUserCards

    /**
     * Проверяет что список карт пользователя корректно маппится в список ответов.
     */
    @Test
    void shouldReturnUserCards_asMappedResponseList() {
        SavedCard anotherCard = Instancio.of(SavedCard.class)
                .set(field(SavedCard::getUser), currentUser)
                .create();

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(savedCardService.getUserCards(currentUser)).thenReturn(List.of(card, anotherCard));

        List<SavedCardResponse> result = cardFacadeService.getUserCards();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(card.getId());
        assertThat(result.get(1).id()).isEqualTo(anotherCard.getId());
    }

    // removeCard

    /**
     * Проверяет что удаление карты делегируется в SavedCardService
     * с корректным пользователем и ID карты.
     */
    @Test
    void shouldRemoveCard_forCurrentUser() {
        when(userService.getCurrentUser()).thenReturn(currentUser);

        cardFacadeService.removeCard(card.getId());

        verify(savedCardService).removeCard(currentUser, card.getId());
    }

    // setDefaultCard

    /**
     * Проверяет установку карты по умолчанию.
     * Возвращает ответ с флагом defaultCard = true.
     */
    @Test
    void shouldSetDefaultCard_andReturnResponse() {
        card.setDefaultCard(true);

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(savedCardService.setDefaultCard(currentUser, card.getId())).thenReturn(card);

        SavedCardResponse result = cardFacadeService.setDefaultCard(card.getId());

        assertThat(result.defaultCard()).isTrue();
        assertThat(result.id()).isEqualTo(card.getId());
        verify(savedCardService).setDefaultCard(currentUser, card.getId());
    }
}
