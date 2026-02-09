package org.resume.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.model.dto.CommonResponse;
import org.resume.paymentservice.model.dto.request.CreatePaymentRequest;
import org.resume.paymentservice.model.dto.response.PaymentResponse;
import org.resume.paymentservice.service.PaymentFacadeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentFacadeService paymentFacadeService;

    @PostMapping
    public CommonResponse<PaymentResponse> createPayment(
            @RequestParam Long userId,
            @RequestBody CreatePaymentRequest createPaymentRequest
    ) {

      PaymentResponse paymentResponse = paymentFacadeService.createPayment(userId, createPaymentRequest);
      return CommonResponse.success(paymentResponse);
    }

    @GetMapping("/{paymentId}")
    public CommonResponse<PaymentResponse> getPayment(@PathVariable String paymentId) {
        PaymentResponse paymentResponse = paymentFacadeService.getPaymentStatus(paymentId);
        return CommonResponse.success(paymentResponse);
    }

}
