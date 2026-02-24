package org.resume.paymentservice.repository;

import org.resume.paymentservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);

    @Modifying
    @Query("UPDATE User u SET u.stripeCustomerId = :customerId WHERE u.id = :id")
    void updateStripeCustomerId(@Param("id") Long id, @Param("customerId") String customerId);

}
