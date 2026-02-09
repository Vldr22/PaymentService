package org.resume.paymentservice.config;

import com.stripe.Stripe;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.properties.StripeProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(StripeProperties.class)
public class StripeConfig {

    @Bean
    public String stripeApiKey(StripeProperties stripeProperties) {
        String apiKey = stripeProperties.getSecretKey();
        Stripe.apiKey = apiKey;

        log.info("Stripe API initialized successfully");
        return apiKey;
    }

}
