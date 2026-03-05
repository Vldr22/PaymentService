package org.resume.paymentservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    @Schema(description = "Stripe PaymentIntent ID", example ="pi_3T4hhvtestyBFap60Hso8KXt")
    private String id;

    @Schema(description = "Статус платежа из Stripe", example = "succeeded",
            allowableValues = {
                    "requires_payment_method",
                    "requires_confirmation",
                    "requires_action",
                    "processing",
                    "succeeded",
                    "canceled"
            })
    private String status;

    @Schema(description = "Сумма платежа", example = "1500.00")
    private Long amount;

    @Schema(description = "Валюта", example = "USD")
    private String currency;

    @Schema(description = "Client secret для подтверждения на фронтенде", example = "pi_3OqX...secret...")
    private String clientSecret;

    @Schema(description = "Описание платежа", example = "Оплата заказа №12345")
    private String description;
}
