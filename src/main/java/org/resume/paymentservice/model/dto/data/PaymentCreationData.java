package org.resume.paymentservice.model.dto.data;

import lombok.Builder;
import lombok.Getter;
import org.resume.paymentservice.model.enums.Currency;

import java.math.BigDecimal;

@Getter
@Builder
public class PaymentCreationData {
    private Long userId;
    private String stripePaymentIntentId;
    private BigDecimal amount;
    private Currency currency;
    private String description;
    private String clientSecret;
}
