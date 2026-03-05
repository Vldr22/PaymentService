package org.resume.paymentservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record SavedCardResponse(
        @Schema(description = "ID карты", example = "42")
        Long id,

        @Schema(description = "Последние 4 цифры номера карты", example = "4242")
        String last4,

        @Schema(description = "Платёжная система", example = "visa")
        String brand,

        @Schema(description = "Месяц истечения срока", example = "12")
        Short expMonth,

        @Schema(description = "Год истечения срока", example = "2027")
        Short expYear,

        @Schema(description = "Является ли карта основной для списаний", example = "true")
        boolean defaultCard
) {
}
