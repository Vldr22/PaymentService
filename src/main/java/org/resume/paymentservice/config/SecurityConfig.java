package org.resume.paymentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/webhooks/**")
                        .ignoringRequestMatchers("/api/payments/**")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/webhooks/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/webhooks/stripe").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/payments").permitAll()
                        .requestMatchers("/api/payments/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}