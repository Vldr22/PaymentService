package org.resume.paymentservice.repository;

import org.resume.paymentservice.model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    Optional<Staff> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE Staff s SET s.password = :password WHERE s.email = :email")
    void updatePassword(@Param("email") String email, @Param("password") String password);

    @Modifying
    @Query("UPDATE Staff s SET s.passwordChangeRequired = false WHERE s.email = :email")
    void clearPasswordChangeRequired(@Param("email") String email);
}
