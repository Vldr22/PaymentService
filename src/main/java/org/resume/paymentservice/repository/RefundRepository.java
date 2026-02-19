package org.resume.paymentservice.repository;

import org.resume.paymentservice.model.entity.Refund;
import org.resume.paymentservice.model.enums.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, Long> {

    List<Refund> findByStatus(RefundStatus status);

    Optional<Refund> findByPaymentStripePaymentIntentIdAndStatus(String stripePaymentIntentId, RefundStatus status);

    boolean existsByPaymentIdAndStatusIn(Long paymentId, List<RefundStatus> statuses);

}
