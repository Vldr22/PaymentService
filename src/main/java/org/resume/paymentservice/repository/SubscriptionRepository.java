package org.resume.paymentservice.repository;

import org.resume.paymentservice.model.entity.Subscription;
import org.resume.paymentservice.model.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByUserId(Long userId);

    Optional<Subscription> findByUserIdAndSubscriptionStatusNot(Long userId, SubscriptionStatus status);

    boolean existsBySavedCardIdAndSubscriptionStatusIn(Long savedCardId, List<SubscriptionStatus> statuses);

    @Query("SELECT s FROM Subscription s " +
            "JOIN FETCH s.user " +
            "JOIN FETCH s.savedCard " +
            "WHERE s.nextBillingDate <= :now AND s.subscriptionStatus IN :statuses")
    List<Subscription> findDueSubscriptions(@Param("now") LocalDateTime now,
                                            @Param("statuses") List<SubscriptionStatus> statuses);
}
