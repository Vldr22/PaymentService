package org.resume.paymentservice.setup;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.model.enums.Roles;
import org.resume.paymentservice.properties.AdminProperties;
import org.resume.paymentservice.service.user.UserService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AdminProperties.class)
public class AdminInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    @PostConstruct
    public void init() {
        if (userService.existsByEmail(adminProperties.getEmail())) {
            return;
        }

        String encodedPassword = passwordEncoder.encode(adminProperties.getPassword());
        userService.createEmployee(
                adminProperties.getName(),
                adminProperties.getSurname(),
                adminProperties.getMidname(),
                adminProperties.getEmail(),
                encodedPassword,
                Roles.ROLE_ADMIN
        );
    }
}
