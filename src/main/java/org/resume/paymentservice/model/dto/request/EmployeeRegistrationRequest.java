package org.resume.paymentservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmployeeRegistrationRequest(
        @Schema(description = "Имя", example = "Иван")
        @NotBlank(message = "Name is required")
        String name,

        @Schema(description = "Фамилия", example = "Иванов")
        @NotBlank(message = "Surname is required")
        String surname,

        @Schema(description = "Отчество (необязательно)", example = "Иванович")
        String midname,

        @Schema(description = "Email сотрудника", example = "ivanov@gmail.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
) {
}
