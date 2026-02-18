package org.resume.paymentservice.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "TempPassword is required")
        @Size(min = 3, max = 50, message = "TempPassword must be between 3 and 50 characters")
        String oldPassword,

        @NotBlank(message = "Password is required")
        @Size(min = 3, max = 50, message = "Password must be between 3 and 50 characters")
        String newPassword

) {
}
