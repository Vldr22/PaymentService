package org.resume.paymentservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.resume.paymentservice.model.enums.Currency;
import org.resume.paymentservice.model.enums.RefundStatus;
import org.resume.paymentservice.utils.BigDecimalToLongConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refunds")
public class Refund {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String yookassaRefundId;

    @Convert(converter = BigDecimalToLongConverter.class)
    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RefundStatus status;

    @Column(length = 500)
    private String reason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

}