package org.resume.paymentservice.service.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.model.dto.response.RefundDetailResponse;
import org.resume.paymentservice.model.entity.Refund;
import org.resume.paymentservice.model.entity.Staff;
import org.resume.paymentservice.model.entity.User;
import org.resume.paymentservice.service.payment.RefundService;
import org.resume.paymentservice.service.payment.StripeService;
import org.resume.paymentservice.service.user.StaffService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportFacadeService {

    private final RefundService refundService;
    private final StripeService stripeService;
    private final StaffService staffService;

    @Transactional(readOnly = true)
    public List<RefundDetailResponse> getPendingRefunds() {
        return refundService.findPendingRefunds().stream()
                .map(this::toRefundDetailResponse)
                .toList();
    }

    @Transactional
    public RefundDetailResponse approveRefund(Long refundId) {
        Staff reviewer = staffService.getCurrentStaff();
        Refund refund = refundService.findById(refundId);
        refundService.validatePendingStatus(refund);

        String stripeRefundId = stripeService.createRefund(
                refund.getPayment().getStripePaymentIntentId(),
                refund.getReason()
        );

        refundService.approveRefund(refund, reviewer, stripeRefundId);

        log.info("Refund approved: refundId={}, stripeRefundId={}", refundId, stripeRefundId);
        return toRefundDetailResponse(refund);
    }

    @Transactional
    public RefundDetailResponse rejectRefund(Long refundId) {
        Staff reviewer = staffService.getCurrentStaff();
        Refund refund = refundService.findById(refundId);
        refundService.validatePendingStatus(refund);

        refundService.rejectRefund(refund, reviewer);

        log.info("Refund rejected: refundId={}", refundId);
        return toRefundDetailResponse(refund);
    }

    private RefundDetailResponse toRefundDetailResponse(Refund refund) {
        User client = refund.getUser();
        return new RefundDetailResponse(
                refund.getId(),
                refund.getStripeRefundId(),
                refund.getPayment().getStripePaymentIntentId(),
                refund.getAmount(),
                refund.getPayment().getCurrency(),
                refund.getReason(),
                refund.getStatus(),
                client.getName(),
                client.getPhone(),
                refund.getCreatedAt(),
                refund.getUpdatedAt()
        );
    }


}
