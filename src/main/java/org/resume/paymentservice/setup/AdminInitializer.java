package org.resume.paymentservice.setup;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.properties.AdminProperties;
import org.resume.paymentservice.service.user.StaffService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AdminProperties.class)
public class AdminInitializer {

    private final StaffService staffService;
    private final AdminProperties adminProperties;

    @PostConstruct
    public void init() {
        staffService.createAdmin(
                adminProperties.getName(),
                adminProperties.getSurname(),
                adminProperties.getMidname(),
                adminProperties.getEmail(),
                adminProperties.getPassword()
        );
    }
}
