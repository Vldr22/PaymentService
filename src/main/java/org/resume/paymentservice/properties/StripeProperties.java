package org.resume.paymentservice.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "stripe")
public class StripeProperties {

    @NotBlank(message = "Stripe secret-key can't be empty")
    private final String secretKey;

    private final String publishableKey;
}
