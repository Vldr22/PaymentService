package org.resume.paymentservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.model.dto.CommonResponse;
import org.resume.paymentservice.model.dto.request.ConfirmPaymentRequest;
import org.resume.paymentservice.model.dto.request.CreatePaymentRequest;
import org.resume.paymentservice.model.dto.request.RefundRequest;
import org.resume.paymentservice.model.dto.response.PaymentResponse;
import org.resume.paymentservice.model.dto.response.RefundResponse;
import org.resume.paymentservice.service.facade.PaymentFacadeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentFacadeService paymentFacadeService;

    @PostMapping
    public CommonResponse<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest createPaymentRequest
    ) {

      PaymentResponse paymentResponse = paymentFacadeService.createPayment(createPaymentRequest);
      return CommonResponse.success(paymentResponse);
    }

    @PostMapping("/{paymentIntentId}/confirm")
    public CommonResponse<PaymentResponse> confirmPayment(
            @PathVariable String paymentIntentId,
            @Valid @RequestBody ConfirmPaymentRequest request
    ) {
        PaymentResponse response = paymentFacadeService.confirmPayment(paymentIntentId, request);
        return CommonResponse.success(response);
    }

    @PostMapping("/{paymentIntentId}/refund")
    public CommonResponse<RefundResponse> requestRefund(
            @PathVariable String paymentIntentId,
            @Valid @RequestBody RefundRequest request
    ) {
        RefundResponse response = paymentFacadeService.createRefund(paymentIntentId, request);
        return CommonResponse.success(response);
    }

    @GetMapping("/{paymentId}")
    public CommonResponse<PaymentResponse> getPayment(@PathVariable String paymentId) {
        PaymentResponse paymentResponse = paymentFacadeService.getPaymentStatus(paymentId);
        return CommonResponse.success(paymentResponse);
    }

}
