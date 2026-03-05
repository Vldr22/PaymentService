package org.resume.paymentservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @Schema(description = "Текущий пароль", example = "OldPass123!")
        @NotBlank(message = "Old password is required")
        @Size(min = 3, max = 50, message = "Old password must be between 3 and 50 characters")
        String oldPassword,

        @Schema(description = "Новый пароль", example = "NewPass456@")
        @NotBlank(message = "New password is required")
        @Size(min = 3, max = 50, message = "New password must be between 3 and 50 characters")
        String newPassword

) {
}
