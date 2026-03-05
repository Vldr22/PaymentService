package org.resume.paymentservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.resume.paymentservice.validation.RussianPhone;

public record SmsCodeRequest(
        @Schema(description = "Номер телефона в российском формате", example = "+70061234567")
        @RussianPhone
        String phone
) {
}
