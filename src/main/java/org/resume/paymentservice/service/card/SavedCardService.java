package org.resume.paymentservice.service.card;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.exception.AlreadyExistsException;
import org.resume.paymentservice.exception.NotFoundException;
import org.resume.paymentservice.model.dto.data.SavedCardData;
import org.resume.paymentservice.model.entity.SavedCard;
import org.resume.paymentservice.model.entity.User;
import org.resume.paymentservice.repository.SavedCardRepository;
import org.resume.paymentservice.service.payment.StripeService;
import org.resume.paymentservice.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavedCardService {

    private final SavedCardRepository savedCardRepository;
    private final UserService userService;
    private final StripeService stripeService;

    @Transactional
    public SavedCard addCard(User user, String paymentMethodId) {
        checkCardAlreadyExists(user, paymentMethodId);

        String customerId = getOrCreateCustomer(user);
        SavedCardData cardData = stripeService.addPaymentMethod(customerId, paymentMethodId);

        SavedCard savedCard = buildSavedCard(user, cardData);
        savedCardRepository.save(savedCard);

        log.info("Card saved: userId={}, paymentMethodId={}", user.getId(), paymentMethodId);
        return savedCard;
    }

    @Transactional(readOnly = true)
    public List<SavedCard> getUserCards(User user) {
        return savedCardRepository.findAllByUser(user);
    }

    @Transactional
    public void removeCard(User user, Long cardId) {
        SavedCard card = getCardByIdAndUser(cardId, user);
        stripeService.removePaymentMethod(card.getStripePaymentMethodId());
        savedCardRepository.delete(card);

        log.info("Card removed: userId={}, cardId={}", user.getId(), cardId);
    }

    @Transactional
    public SavedCard setDefaultCard(User user, Long cardId) {
        SavedCard card = getCardByIdAndUser(cardId, user);

        savedCardRepository.resetDefaultCard(user);
        savedCardRepository.setDefaultCard(cardId, user);

        card.setDefaultCard(true);

        log.info("Default card updated: userId={}, cardId={}", user.getId(), cardId);
        return card;
    }

    // ========== PRIVATE ==========
    private String getOrCreateCustomer(User user) {
        if (user.getStripeCustomerId() != null) {
            return user.getStripeCustomerId();
        }

        String customerId = stripeService.createCustomer(
                user.getName() + " " + user.getSurname(),
                user.getPhone()
        );

        userService.updateStripeCustomerId(user, customerId);
        return customerId;
    }

    private void checkCardAlreadyExists(User user, String paymentMethodId) {
        if (savedCardRepository.existsByStripePaymentMethodIdAndUser(paymentMethodId, user)) {
            throw AlreadyExistsException.cardAlreadyPresent(paymentMethodId);
        }
    }

    public SavedCard getCardByIdAndUser(Long cardId, User user) {
        return savedCardRepository.findByIdAndUser(cardId, user)
                .orElseThrow(() -> NotFoundException.cardById(cardId));
    }

    private SavedCard buildSavedCard(User user, SavedCardData data) {
        SavedCard card = new SavedCard();
        card.setStripePaymentMethodId(data.stripePaymentMethodId());
        card.setLast4(data.last4());
        card.setBrand(data.brand());
        card.setExpMonth(data.expMonth());
        card.setExpYear(data.expYear());
        card.setDefaultCard(false);
        card.setUser(user);
        return card;
    }
}
