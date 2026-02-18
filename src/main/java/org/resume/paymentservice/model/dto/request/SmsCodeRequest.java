package org.resume.paymentservice.model.dto.request;

import org.resume.paymentservice.validation.RussianPhone;

public record SmsCodeRequest(
        @RussianPhone
        String phone
) {
}
