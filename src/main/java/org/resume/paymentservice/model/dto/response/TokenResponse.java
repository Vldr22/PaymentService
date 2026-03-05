package org.resume.paymentservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponse(
        @Schema(description = "JWT Bearer токен", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token
) {
}
