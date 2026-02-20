package org.resume.paymentservice.service.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.model.dto.request.AddCardRequest;
import org.resume.paymentservice.model.dto.response.SavedCardResponse;
import org.resume.paymentservice.model.entity.SavedCard;
import org.resume.paymentservice.model.entity.User;
import org.resume.paymentservice.service.card.SavedCardService;
import org.resume.paymentservice.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardFacadeService {

    private final SavedCardService savedCardService;
    private final UserService userService;

    public SavedCardResponse addCard(AddCardRequest request) {
        User user = userService.getCurrentUser();
        SavedCard card = savedCardService.addCard(user, request.paymentMethodId());
        return toSavedCardResponse(card);
    }

    public List<SavedCardResponse> getUserCards() {
        User user = userService.getCurrentUser();
        return savedCardService.getUserCards(user)
                .stream()
                .map(this::toSavedCardResponse)
                .toList();
    }

    public void removeCard(Long cardId) {
        User user = userService.getCurrentUser();
        savedCardService.removeCard(user, cardId);
    }

    public SavedCardResponse setDefaultCard(Long cardId) {
        User user = userService.getCurrentUser();
        SavedCard card = savedCardService.setDefaultCard(user, cardId);
        return toSavedCardResponse(card);
    }

    private SavedCardResponse toSavedCardResponse(SavedCard card) {
        return new SavedCardResponse(
                card.getId(),
                card.getLast4(),
                card.getBrand(),
                card.getExpMonth(),
                card.getExpYear(),
                card.isDefaultCard()
        );
    }
}
