package org.resume.paymentservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.resume.paymentservice.model.enums.UserStatus;

public record ClientResponse(
        @Schema(description = "Имя", example = "Иван")
        String name,

        @Schema(description = "Фамилия", example = "Иванов")
        String surname,

        @Schema(description = "Номер телефона", example = "+79001234567")
        String phone,

        @Schema(description = "Статус аккаунта", example = "ACTIVE")
        UserStatus status
) {
}
