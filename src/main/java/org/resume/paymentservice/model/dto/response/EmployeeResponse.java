package org.resume.paymentservice.model.dto.response;

import org.resume.paymentservice.model.enums.Roles;
import org.resume.paymentservice.model.enums.UserStatus;

public record EmployeeResponse(
        String name,
        String surname,
        String email,
        Roles role,
        UserStatus status
) {
}
