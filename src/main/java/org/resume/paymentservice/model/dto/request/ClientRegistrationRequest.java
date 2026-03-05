package org.resume.paymentservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.resume.paymentservice.validation.RussianPhone;

public record ClientRegistrationRequest(
        @Schema(description = "Имя", example = "Иван")
        @NotBlank(message = "Name is required")
        String name,

        @Schema(description = "Фамилия", example = "Иванов")
        @NotBlank(message = "Surname is required")
        String surname,

        @Schema(description = "Отчество (необязательно)", example = "Иванович")
        String midname,

        @Schema(description = "Номер телефона в российском формате", example = "+79001234567")
        @RussianPhone
        String phone
) {
}
