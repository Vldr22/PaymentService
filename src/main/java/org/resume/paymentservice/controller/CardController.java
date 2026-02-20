package org.resume.paymentservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.model.dto.CommonResponse;
import org.resume.paymentservice.model.dto.request.AddCardRequest;
import org.resume.paymentservice.model.dto.response.SavedCardResponse;
import org.resume.paymentservice.service.facade.CardFacadeService;
import org.resume.paymentservice.utils.SuccessMessages;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {

    private final CardFacadeService cardFacadeService;

    @PostMapping
    public CommonResponse<SavedCardResponse> addCard(
            @Valid @RequestBody AddCardRequest request
    ) {
        return CommonResponse.success(cardFacadeService.addCard(request));
    }

    @GetMapping
    public CommonResponse<List<SavedCardResponse>> getUserCards() {
        return CommonResponse.success(cardFacadeService.getUserCards());
    }

    @DeleteMapping("/{cardId}")
    public CommonResponse<String> removeCard(@PathVariable Long cardId) {
        cardFacadeService.removeCard(cardId);
        return CommonResponse.success(SuccessMessages.CARD_REMOVED_SUCCESS);
    }

    @PatchMapping("/{cardId}/default")
    public CommonResponse<SavedCardResponse> setDefaultCard(@PathVariable Long cardId) {
        return CommonResponse.success(cardFacadeService.setDefaultCard(cardId));
    }
}
