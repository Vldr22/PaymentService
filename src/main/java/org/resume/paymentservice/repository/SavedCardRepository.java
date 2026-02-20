package org.resume.paymentservice.repository;

import org.resume.paymentservice.model.entity.SavedCard;
import org.resume.paymentservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SavedCardRepository extends JpaRepository<SavedCard, Long> {

    @Modifying
    @Query("UPDATE SavedCard c SET c.defaultCard = false WHERE c.user = :user")
    void resetDefaultCard(@Param("user") User user);

    @Modifying
    @Query("UPDATE SavedCard c SET c.defaultCard = true WHERE c.id = :id AND c.user = :user")
    void setDefaultCard(@Param("id") Long id, @Param("user") User user);

    List<SavedCard> findAllByUser(User user);

    Optional<SavedCard> findByIdAndUser(Long id, User user);

    boolean existsByStripePaymentMethodIdAndUser(String stripePaymentMethodId, User user);
}
