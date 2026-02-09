package org.resume.paymentservice.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String id;
    private String status;
    private Long amount;
    private String currency;
    private String clientSecret;
    private String description;
}
