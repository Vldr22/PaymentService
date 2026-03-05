package org.resume.paymentservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SupportLoginRequest(
        @Schema(description = "Email сотрудника", example = "ivanov@gmail.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "Пароль", example = "NewPass456@")
        @NotBlank(message = "Password is required")
        @Size(min = 3, max = 50, message = "Password must be between 3 and 50 characters")
        String password

) {
}
