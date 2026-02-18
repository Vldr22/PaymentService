package org.resume.paymentservice.properties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "admin")
public class AdminProperties {
    @NotBlank(message = "Admin name is required")
    private final String name;

    @NotBlank(message = "Admin surname is required")
    private final String surname;

    private final String midname;

    @NotBlank(message = "Admin Email is required")
    @Email(message = "Invalid email format for Admin registration")
    private final String email;

    @NotBlank(message = "Admin Password is required")
    @Size(min = 3, max = 50, message = "Password must be between 3 and 50 characters")
    private final String password;
}
