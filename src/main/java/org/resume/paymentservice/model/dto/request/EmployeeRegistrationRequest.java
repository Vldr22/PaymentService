package org.resume.paymentservice.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmployeeRegistrationRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Surname is required")
        String surname,

        String midname,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
) {
}
