package org.resume.paymentservice.service.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.resume.paymentservice.exception.AlreadyExistsException;
import org.resume.paymentservice.exception.NotFoundException;
import org.resume.paymentservice.exception.PaymentException;
import org.resume.paymentservice.model.entity.Payment;
import org.resume.paymentservice.model.entity.Refund;
import org.resume.paymentservice.model.entity.User;
import org.resume.paymentservice.model.enums.PaymentStatus;
import org.resume.paymentservice.model.enums.RefundReason;
import org.resume.paymentservice.model.enums.RefundStatus;
import org.resume.paymentservice.repository.RefundRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;

    public Refund createRefundRequest(Payment payment, User user, RefundReason reason) {
        validateRefundable(payment);
        validateNoDuplicateRequest(payment);

        Refund refund = new Refund(payment, user, reason);
        Refund savedRefund = refundRepository.save(refund);

        log.info("Refund request created: id={}, paymentId={}, reason={}",
                savedRefund.getId(), payment.getId(), reason);

        return savedRefund;
    }

    public Refund findById(Long refundId) {
        return refundRepository.findById(refundId)
                .orElseThrow(() -> NotFoundException.refundById(refundId));
    }

    public List<Refund> findPendingRefunds() {
        return refundRepository.findByStatus(RefundStatus.PENDING);
    }


    public void approveRefund(Refund refund, User reviewer, String stripeRefundId) {
        refund.setStatus(RefundStatus.APPROVED);
        refund.setStripeRefundId(stripeRefundId);
        refund.setReviewedBy(reviewer);
        refundRepository.save(refund);

        log.info("Refund approved: id={}, reviewedBy={}", refund.getId(), reviewer.getEmail());
    }

    public void rejectRefund(Refund refund, User reviewer) {
        refund.setStatus(RefundStatus.REJECTED);
        refund.setReviewedBy(reviewer);
        refundRepository.save(refund);

        log.info("Refund rejected: id={}, reviewedBy={}", refund.getId(), reviewer.getEmail());
    }

    public void updateRefundStatusByPaymentIntentId(String paymentIntentId, RefundStatus status) {
        Refund refund = refundRepository.findByPaymentStripePaymentIntentIdAndStatus(
                paymentIntentId, RefundStatus.APPROVED
        ).orElseThrow(() -> NotFoundException.refundByPaymentIntentId(paymentIntentId));

        RefundStatus oldStatus = refund.getStatus();
        refund.setStatus(status);
        refundRepository.save(refund);

        log.info("Refund status updated: paymentIntentId={}, {} -> {}", paymentIntentId, oldStatus, status);
    }

    public void validatePendingStatus(Refund refund) {
        if (refund.getStatus() != RefundStatus.PENDING) {
            throw PaymentException.refundNotPending(
                    String.valueOf(refund.getId()),
                    refund.getStatus().name()
            );
        }
    }

    private void validateRefundable(Payment payment) {
        if (payment.getStatus() != PaymentStatus.SUCCEEDED) {
            throw PaymentException.notRefundable(
                    payment.getStripePaymentIntentId(),
                    payment.getStatus().name()
            );
        }
    }

    private void validateNoDuplicateRequest(Payment payment) {
        boolean exists = refundRepository.existsByPaymentIdAndStatusIn(
                payment.getId(),
                List.of(RefundStatus.PENDING, RefundStatus.APPROVED)
        );

        if (exists) {
            throw AlreadyExistsException.refundByPaymentId(payment.getId());
        }
    }
}