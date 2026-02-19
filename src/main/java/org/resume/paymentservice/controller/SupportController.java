package org.resume.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.resume.paymentservice.model.dto.CommonResponse;
import org.resume.paymentservice.model.dto.response.RefundDetailResponse;
import org.resume.paymentservice.service.facade.SupportFacadeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/support")
public class SupportController {

    private final SupportFacadeService supportFacadeService;

    @GetMapping("/refunds/pending")
    public CommonResponse<List<RefundDetailResponse>> getPendingRefunds() {
        List<RefundDetailResponse> refunds = supportFacadeService.getPendingRefunds();
        return CommonResponse.success(refunds);
    }

    @PostMapping("/refunds/{refundId}/approve")
    public CommonResponse<RefundDetailResponse> approveRefund(@PathVariable Long refundId) {
        RefundDetailResponse response = supportFacadeService.approveRefund(refundId);
        return CommonResponse.success(response);
    }

    @PostMapping("/refunds/{refundId}/reject")
    public CommonResponse<RefundDetailResponse> rejectRefund(@PathVariable Long refundId) {
        RefundDetailResponse response = supportFacadeService.rejectRefund(refundId);
        return CommonResponse.success(response);
    }

}
