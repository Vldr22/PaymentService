package org.resume.paymentservice.repository;

import org.resume.paymentservice.model.entity.BillingAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillingAttemptRepository extends JpaRepository<BillingAttempt, Long> {

    List<BillingAttempt> findAllBySubscriptionId(Long subscriptionId);

    Optional<BillingAttempt> findByStripePaymentIntentId(String stripePaymentIntentId);

}
