package org.resume.paymentservice.model.dto.response;

import org.resume.paymentservice.model.enums.UserStatus;

public record ClientResponse(
        String name,
        String surname,
        String phone,
        UserStatus status
) {
}
