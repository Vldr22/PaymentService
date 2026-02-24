package org.resume.paymentservice.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SetInitialPasswordRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Temporary password is required")
        String tempPassword,

        @NotBlank(message = "New password is required")
        @Size(min = 3, max = 50, message = "Password must be between 3 and 50 characters")
        String newPassword
) {
}
