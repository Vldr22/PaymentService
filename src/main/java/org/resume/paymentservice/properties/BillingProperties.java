package org.resume.paymentservice.properties;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.model.enums.Currency;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Getter
@Validated
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "billing")
public class BillingProperties {

    @Positive(message = "Billing max retry count must be positive")
    private final int maxRetryCount;


    @Positive(message = "Billing retry interval days must be positive")
    private final int retryIntervalDays;

    @Positive(message = "Billing interval days must be positive")
    private final int intervalDays;

    @NotNull(message = "Billing basic amount is required")
    private final BigDecimal basicAmount;

    @NotNull(message = "Billing premium amount is required")
    private final BigDecimal premiumAmount;

    @NotNull(message = "Currency installation is required")
    private final Currency defaultCurrency;

}
