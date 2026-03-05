package org.resume.paymentservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SetInitialPasswordRequest(
        @Schema(description = "Email сотрудника", example = "ivanov@gmail.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "Временный пароль", example = "Temp12345")
        @NotBlank(message = "Temporary password is required")
        String tempPassword,

        @Schema(description = "Новый пароль", example = "NewPass456@")
        @NotBlank(message = "New password is required")
        @Size(min = 3, max = 50, message = "Password must be between 3 and 50 characters")
        String newPassword
) {
}
